package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.AuditLogEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuditLogEntityRepository extends ReactiveCrudRepository<AuditLogEntity, UUID> {
    Flux<AuditLogEntity> findByPrescriptionId(UUID prescriptionId);

    Flux<AuditLogEntity> findByPharmacyId(UUID pharmacyId);
}
