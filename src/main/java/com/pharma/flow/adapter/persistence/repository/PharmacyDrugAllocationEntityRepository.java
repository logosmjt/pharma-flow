package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.PharmacyDrugAllocationEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PharmacyDrugAllocationEntityRepository
        extends ReactiveCrudRepository<PharmacyDrugAllocationEntity, UUID> {
    Flux<PharmacyDrugAllocationEntity> findByPharmacyId(UUID pharmacyId);

    @Query("SELECT COALESCE(SUM(allocation_limit), 0) FROM pharmacy_drug_allocation WHERE drug_id = :drugId")
    Mono<Integer> sumAllocationByDrugId(@Param("drugId") UUID drugId);

    Mono<PharmacyDrugAllocationEntity> findByPharmacyIdAndDrugId(UUID pharmacyId, UUID drugId);
}
