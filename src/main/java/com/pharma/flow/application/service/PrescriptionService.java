package com.pharma.flow.application.service;

import com.pharma.flow.adapter.persistence.mapper.PrescriptionMapper;
import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.adapter.web.exception.UnprocessableException;
import com.pharma.flow.domain.model.AuditLog;
import com.pharma.flow.domain.model.Drug;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionStatus;
import com.pharma.flow.domain.repository.AuditLogRepository;
import com.pharma.flow.domain.repository.DrugRepository;
import com.pharma.flow.domain.repository.PharmacyRepository;
import com.pharma.flow.domain.repository.PrescriptionRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final DrugRepository drugRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AuditLogRepository auditLogRepository;
    private final TransactionalOperator tx;
    private final PrescriptionMapper mapper;

    public Mono<Prescription> getPrescriptionById(UUID id) {
        return prescriptionRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Prescription not found")));
    }

    public Mono<Prescription> savePrescription(Prescription prescription) {
        Map<UUID, Drug> drugCache = new HashMap<>();
        Map<UUID, PharmacyDrugAllocation> allocationCache = new HashMap<>();
        return validatePrescription(prescription, drugCache, allocationCache)
                .then(Mono.defer(() -> {
                    prescription.setStatus(PrescriptionStatus.PENDING);
                    prescription.setFulfilledAt(null);
                    return prescriptionRepository.savePrescription(prescription);
                }))
                .as(tx::transactional);
    }

    public Mono<Prescription> fulfillment(UUID id) {

        Map<UUID, Drug> drugCache = new HashMap<>();
        Map<UUID, PharmacyDrugAllocation> allocationCache = new HashMap<>();
        return prescriptionRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Prescription not found")))
                .flatMap(prescription -> validatePrescription(prescription, drugCache, allocationCache)
                        .then(Mono.defer(() -> applyFulfillment(prescription, drugCache, allocationCache)))
                        .onErrorResume(ex -> {
                            log.error("Error fulfilling prescription: {}", ex.getMessage(), ex);
                            AuditLog log = mapper.buildAuditLog(prescription, false, ex.getMessage());
                            return auditLogRepository
                                    .save(log)
                                    .then(prescriptionRepository.updateStatus(id, PrescriptionStatus.FAILED, null));
                        }))
                .as(tx::transactional);
    }

    private Mono<Void> validatePrescription(
            Prescription prescription, Map<UUID, Drug> drugCache, Map<UUID, PharmacyDrugAllocation> allocationCache) {
        UUID pharmacyId = prescription.getPharmacyId();

        return Flux.fromIterable(prescription.getDrugs())
                .flatMap(prescriptionDrug -> {
                    UUID drugId = prescriptionDrug.getDrugId();
                    int requiredQty = prescriptionDrug.getQuantity();
                    log.debug("drugId: {}, requiredQty: {}", drugId, requiredQty);

                    return drugRepository
                            .findById(drugId)
                            .switchIfEmpty(Mono.error(new NotFoundException("Drug not found: " + drugId)))
                            .flatMap(drug -> {
                                drugCache.put(drugId, drug);
                                if (drug.getExpiryDate().isBefore(LocalDate.now())) {
                                    return Mono.error(new UnprocessableException("Drug expired: " + drugId));
                                }
                                if (drug.getStock() < requiredQty) {
                                    return Mono.error(
                                            new UnprocessableException("Insufficient stock for drug: " + drugId));
                                }

                                return pharmacyRepository
                                        .findByPharmacyIdAndDrugId(pharmacyId, drugId)
                                        .switchIfEmpty(
                                                Mono.error(new NotFoundException("Not found allocation pharmacyId: "
                                                        + pharmacyId + ", drugId: " + drugId)))
                                        .flatMap(allocation -> {
                                            allocationCache.put(drugId, allocation);
                                            if (allocation.getAllocationLimit() < requiredQty) {
                                                return Mono.error(new UnprocessableException(
                                                        "Allocation limit exceeded for drug: " + drugId));
                                            }
                                            return Mono.empty();
                                        });
                            });
                })
                .then();
    }

    public Mono<Prescription> applyFulfillment(
            Prescription prescription, Map<UUID, Drug> drugCache, Map<UUID, PharmacyDrugAllocation> allocationCache) {
        return Flux.fromIterable(prescription.getDrugs())
                .flatMap(prescriptionDrug -> {
                    UUID drugId = prescriptionDrug.getDrugId();
                    int qty = prescriptionDrug.getQuantity();

                    // 更新库存
                    Drug drug = drugCache.get(drugId);
                    drug.setStock(drug.getStock() - qty);
                    Mono<Void> updateStock = drugRepository.save(drug).then();

                    // 更新 allocation limit
                    PharmacyDrugAllocation allocation = allocationCache.get(drugId);
                    allocation.setAllocationLimit(allocation.getAllocationLimit() - qty);
                    Mono<Void> updateAllocation =
                            pharmacyRepository.saveAllocation(allocation).then();

                    return updateStock.then(updateAllocation);
                })
                .then(Mono.defer(() -> {
                    AuditLog log = mapper.buildAuditLog(prescription, true, null);
                    return auditLogRepository
                            .save(log)
                            .then(prescriptionRepository.updateStatus(
                                    prescription.getId(), PrescriptionStatus.FULFILLED, Instant.now()));
                }));
    }
}
