package com.pharma.flow.adapter.persistence.adapter;

import com.pharma.flow.adapter.persistence.entity.PrescriptionDrugEntity;
import com.pharma.flow.adapter.persistence.entity.PrescriptionEntity;
import com.pharma.flow.adapter.persistence.mapper.PrescriptionMapper;
import com.pharma.flow.adapter.persistence.repository.PrescriptionDrugEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PrescriptionEntityRepository;
import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionStatus;
import com.pharma.flow.domain.repository.PrescriptionRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class PrescriptionAdapter implements PrescriptionRepository {
    private final PrescriptionEntityRepository prescriptionEntityRepository;
    private final PrescriptionDrugEntityRepository prescriptionDrugEntityRepository;
    private final PrescriptionMapper mapper;

    @Override
    public Mono<Prescription> findById(UUID id) {
        return Mono.zip(
                        prescriptionEntityRepository.findById(id),
                        prescriptionDrugEntityRepository
                                .findByPrescriptionId(id)
                                .collectList())
                .map(tuple -> mapper.toDomain(tuple.getT1(), tuple.getT2()));
    }

    @Override
    public Mono<Prescription> updateStatus(UUID id, PrescriptionStatus status, Instant fulfilledAt) {
        return prescriptionEntityRepository
                .findById(id)
                .flatMap(entity -> {
                    entity.setStatus(status.name());
                    if (Objects.nonNull(fulfilledAt)) entity.setFulfilledAt(fulfilledAt);
                    return prescriptionEntityRepository.save(entity);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Prescription> savePrescription(Prescription prescription) {
        PrescriptionEntity entity = mapper.toEntity(prescription);
        return prescriptionEntityRepository.save(entity).flatMap(saved -> {
            prescription.getDrugs().forEach(it -> it.setPrescriptionId(saved.getId()));
            log.debug(
                    "Saving prescription with id {}",
                    prescription.getDrugs().get(0).getPrescriptionId());
            List<PrescriptionDrugEntity> drugEntities = mapper.toEntityList(prescription.getDrugs());
            return prescriptionDrugEntityRepository
                    .saveAll(drugEntities)
                    .collectList()
                    .map(savedDrugs -> mapper.toDomain(saved, savedDrugs));
        });
    }
}
