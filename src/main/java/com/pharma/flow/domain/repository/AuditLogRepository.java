package com.pharma.flow.domain.repository;

import com.pharma.flow.domain.model.AuditLog;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuditLogRepository {
    Mono<AuditLog> save(AuditLog auditLog);

    Mono<AuditLog> findById(UUID id);

    Flux<AuditLog> findByPrescriptionId(UUID prescriptionId);

    Flux<AuditLog> findByPharmacyId(UUID pharmacyId);
}
