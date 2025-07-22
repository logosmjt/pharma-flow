package com.pharma.flow.adapter.persistence.adapter;

import com.pharma.flow.adapter.persistence.entity.PharmacyDrugAllocationEntity;
import com.pharma.flow.adapter.persistence.mapper.PharmacyMapper;
import com.pharma.flow.adapter.persistence.repository.DrugEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PharmacyDrugAllocationEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PharmacyEntityRepository;
import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.adapter.web.exception.UnprocessableException;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import com.pharma.flow.domain.repository.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PharmacyAdapter implements PharmacyRepository {
    private final PharmacyEntityRepository pharmacyEntityRepository;
    private final PharmacyDrugAllocationEntityRepository allocationRepository;
    private final DrugEntityRepository drugRepository;
    private final PharmacyMapper mapper;

    @Override
    public Mono<Pharmacy> findById(UUID pharmacyId) {
        return Mono.zip(
                        pharmacyEntityRepository.findById(pharmacyId),
                        allocationRepository.findByPharmacyId(pharmacyId).collectList())
                .map(tuple -> mapper.toDomain(tuple.getT1(), tuple.getT2()));
    }

    @Override
    public Flux<Pharmacy> findAll() {
        return pharmacyEntityRepository.findAll().flatMap(pharmacy -> allocationRepository
                .findByPharmacyId(pharmacy.getId())
                .collectList()
                .map(allocations -> mapper.toDomain(pharmacy, allocations)));
    }

    @Override
    public Mono<Pharmacy> savePharmacy(Pharmacy pharmacy) {
        return pharmacyEntityRepository.save(mapper.toEntity(pharmacy)).map(mapper::toDomainWithOutAllocation);
    }

    @Override
    public Mono<PharmacyDrugAllocation> findAllocationById(UUID allocationId) {
        return allocationRepository.findById(allocationId).map(mapper::toDomain);
    }

    @Override
    public Mono<PharmacyDrugAllocation> findByPharmacyIdAndDrugId(UUID pharmacyId, UUID drugId) {
        return allocationRepository
                .findByPharmacyIdAndDrugId(pharmacyId, drugId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<PharmacyDrugAllocation> saveAllocation(PharmacyDrugAllocation allocation) {
        return drugRepository
                .findById(allocation.getDrugId())
                .switchIfEmpty(Mono.error(new NotFoundException("Drug not found")))
                .flatMap(drugEntity -> allocationRepository
                        .sumAllocationByDrugId(allocation.getDrugId())
                        .defaultIfEmpty(0)
                        .flatMap(totalAllocated -> {
                            log.debug(
                                    "totalAllocated: {}, allocationLimit: {}",
                                    totalAllocated,
                                    allocation.getAllocationLimit());
                            // 如果是更新场景，需要减去原本的 allocationLimit
                            Mono<Integer> existingLimitMono = allocation.getId() != null
                                    ? allocationRepository
                                            .findById(allocation.getId())
                                            .map(PharmacyDrugAllocationEntity::getAllocationLimit)
                                            .defaultIfEmpty(0)
                                    : Mono.just(0);

                            return existingLimitMono.flatMap(existingLimit -> {
                                int adjustedTotal = totalAllocated - existingLimit + allocation.getAllocationLimit();

                                if (adjustedTotal > drugEntity.getStock()) {
                                    return Mono.error(new UnprocessableException("Allocation exceeds available stock"));
                                }

                                PharmacyDrugAllocationEntity entity = mapper.toEntity(allocation);
                                return allocationRepository.save(entity).map(mapper::toDomain);
                            });
                        }));
    }
}
