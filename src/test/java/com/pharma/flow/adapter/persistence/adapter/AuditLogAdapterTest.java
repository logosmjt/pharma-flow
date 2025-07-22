package com.pharma.flow.adapter.persistence.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.persistence.entity.AuditLogEntity;
import com.pharma.flow.adapter.persistence.mapper.AuditLogMapper;
import com.pharma.flow.adapter.persistence.repository.AuditLogEntityRepository;
import com.pharma.flow.domain.model.AuditLog;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuditLogAdapterTest {

    private final AuditLogEntityRepository repository = Mockito.mock(AuditLogEntityRepository.class);
    private final AuditLogMapper mapper = Mockito.mock(AuditLogMapper.class);
    private final AuditLogAdapter adapter = new AuditLogAdapter(repository, mapper);

    @Test
    void shouldSaveAuditLog() {
        // given
        AuditLog auditLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .prescriptionId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .pharmacyId(UUID.randomUUID())
                .requestedDrugs("{\"drug1\": 2}")
                .dispensedDrugs("{\"drug1\": 2}")
                .success(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        AuditLogEntity entity = new AuditLogEntity();
        Mockito.when(mapper.toEntity(any(AuditLog.class))).thenReturn(entity);
        Mockito.when(repository.save(any(AuditLogEntity.class))).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomain(any(AuditLogEntity.class))).thenReturn(auditLog);

        // when
        Mono<AuditLog> result = adapter.save(auditLog);

        // then
        StepVerifier.create(result).expectNext(auditLog).verifyComplete();
        Mockito.verify(repository).save(any(AuditLogEntity.class));
        Mockito.verify(mapper).toEntity(eq(auditLog));
        Mockito.verify(mapper).toDomain(any(AuditLogEntity.class));
    }

    @Test
    void shouldFindAuditLogById() {
        // given
        UUID id = UUID.randomUUID();
        AuditLog auditLog = AuditLog.builder()
                .id(id)
                .prescriptionId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .pharmacyId(UUID.randomUUID())
                .requestedDrugs("{\"drug1\": 2}")
                .dispensedDrugs("{\"drug1\": 2}")
                .success(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        AuditLogEntity entity = new AuditLogEntity();
        Mockito.when(repository.findById(eq(id))).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomain(any(AuditLogEntity.class))).thenReturn(auditLog);

        // when
        Mono<AuditLog> result = adapter.findById(id);

        // then
        StepVerifier.create(result).expectNext(auditLog).verifyComplete();
        Mockito.verify(repository).findById(eq(id));
        Mockito.verify(mapper).toDomain(any(AuditLogEntity.class));
    }

    @Test
    void shouldFindAuditLogsByPrescriptionId() {
        // given
        UUID prescriptionId = UUID.randomUUID();
        AuditLog auditLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .prescriptionId(prescriptionId)
                .patientId(UUID.randomUUID())
                .pharmacyId(UUID.randomUUID())
                .requestedDrugs("{\"drug1\": 2}")
                .dispensedDrugs("{\"drug1\": 2}")
                .success(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        AuditLogEntity entity = new AuditLogEntity();
        Mockito.when(repository.findByPrescriptionId(eq(prescriptionId))).thenReturn(Flux.just(entity));
        Mockito.when(mapper.toDomain(any(AuditLogEntity.class))).thenReturn(auditLog);

        // when
        Flux<AuditLog> result = adapter.findByPrescriptionId(prescriptionId);

        // then
        StepVerifier.create(result).expectNext(auditLog).verifyComplete();
        Mockito.verify(repository).findByPrescriptionId(eq(prescriptionId));
        Mockito.verify(mapper).toDomain(any(AuditLogEntity.class));
    }

    @Test
    void shouldFindAuditLogsByPharmacyId() {
        // given
        UUID pharmacyId = UUID.randomUUID();
        AuditLog auditLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .prescriptionId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .pharmacyId(pharmacyId)
                .requestedDrugs("{\"drug1\": 2}")
                .dispensedDrugs("{\"drug1\": 2}")
                .success(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        AuditLogEntity entity = new AuditLogEntity();
        Mockito.when(repository.findByPharmacyId(eq(pharmacyId))).thenReturn(Flux.just(entity));
        Mockito.when(mapper.toDomain(any(AuditLogEntity.class))).thenReturn(auditLog);

        // when
        Flux<AuditLog> result = adapter.findByPharmacyId(pharmacyId);

        // then
        StepVerifier.create(result).expectNext(auditLog).verifyComplete();
        Mockito.verify(repository).findByPharmacyId(eq(pharmacyId));
        Mockito.verify(mapper).toDomain(any(AuditLogEntity.class));
    }
}
