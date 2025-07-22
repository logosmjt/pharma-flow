package com.pharma.flow.domain.repository;

import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PharmacyRepository {
    Mono<Pharmacy> findById(UUID pharmacyId);

    Flux<Pharmacy> findAll();

    Mono<Pharmacy> savePharmacy(Pharmacy pharmacy);

    Mono<PharmacyDrugAllocation> findAllocationById(UUID allocationId);

    Mono<PharmacyDrugAllocation> findByPharmacyIdAndDrugId(UUID pharmacyId, UUID drugId);

    Mono<PharmacyDrugAllocation> saveAllocation(PharmacyDrugAllocation allocation);
}
