package com.pharma.flow.adapter.persistence.adapter;

import com.pharma.flow.adapter.persistence.mapper.AuditLogMapper;
import com.pharma.flow.adapter.persistence.repository.AuditLogEntityRepository;
import com.pharma.flow.domain.model.AuditLog;
import com.pharma.flow.domain.repository.AuditLogRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuditLogAdapter implements AuditLogRepository {

    private final AuditLogEntityRepository repository;
    private final AuditLogMapper mapper;

    @Override
    public Mono<AuditLog> save(AuditLog auditLog) {
        return repository.save(mapper.toEntity(auditLog)).map(mapper::toDomain);
    }

    @Override
    public Mono<AuditLog> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Flux<AuditLog> findByPrescriptionId(UUID prescriptionId) {
        return repository.findByPrescriptionId(prescriptionId).map(mapper::toDomain);
    }

    @Override
    public Flux<AuditLog> findByPharmacyId(UUID pharmacyId) {
        return repository.findByPharmacyId(pharmacyId).map(mapper::toDomain);
    }
}
