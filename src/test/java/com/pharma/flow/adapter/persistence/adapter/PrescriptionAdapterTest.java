package com.pharma.flow.adapter.persistence.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.persistence.entity.PrescriptionDrugEntity;
import com.pharma.flow.adapter.persistence.entity.PrescriptionEntity;
import com.pharma.flow.adapter.persistence.mapper.PrescriptionMapper;
import com.pharma.flow.adapter.persistence.repository.PrescriptionDrugEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PrescriptionEntityRepository;
import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionDrug;
import com.pharma.flow.domain.model.PrescriptionStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PrescriptionAdapterTest {

    private final PrescriptionEntityRepository prescriptionEntityRepository =
            Mockito.mock(PrescriptionEntityRepository.class);
    private final PrescriptionDrugEntityRepository prescriptionDrugEntityRepository =
            Mockito.mock(PrescriptionDrugEntityRepository.class);
    private final PrescriptionMapper mapper = Mockito.mock(PrescriptionMapper.class);
    private final PrescriptionAdapter adapter =
            new PrescriptionAdapter(prescriptionEntityRepository, prescriptionDrugEntityRepository, mapper);

    @Test
    void shouldFindPrescriptionById() {
        // given
        UUID id = UUID.randomUUID();
        PrescriptionEntity entity = new PrescriptionEntity();
        List<PrescriptionDrugEntity> drugEntities = List.of(new PrescriptionDrugEntity());
        Prescription prescription = Prescription.builder().id(id).build();

        Mockito.when(prescriptionEntityRepository.findById(eq(id))).thenReturn(Mono.just(entity));
        Mockito.when(prescriptionDrugEntityRepository.findByPrescriptionId(eq(id)))
                .thenReturn(Flux.fromIterable(drugEntities));
        Mockito.when(mapper.toDomain(eq(entity), any())).thenReturn(prescription);

        // when
        Mono<Prescription> result = adapter.findById(id);

        // then
        StepVerifier.create(result).expectNext(prescription).verifyComplete();
        Mockito.verify(prescriptionEntityRepository).findById(eq(id));
        Mockito.verify(prescriptionDrugEntityRepository).findByPrescriptionId(eq(id));
        Mockito.verify(mapper).toDomain(eq(entity), any());
    }

    @Test
    void shouldUpdatePrescriptionStatus() {
        // given
        UUID id = UUID.randomUUID();
        PrescriptionEntity entity = new PrescriptionEntity();
        entity.setId(id);
        entity.setStatus(PrescriptionStatus.PENDING.name());
        Prescription updatedPrescription = Prescription.builder()
                .id(id)
                .status(PrescriptionStatus.FULFILLED)
                .build();

        Mockito.when(prescriptionEntityRepository.findById(eq(id))).thenReturn(Mono.just(entity));
        Mockito.when(prescriptionEntityRepository.save(any(PrescriptionEntity.class)))
                .thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomain(eq(entity))).thenReturn(updatedPrescription);

        // when
        Mono<Prescription> result = adapter.updateStatus(id, PrescriptionStatus.FULFILLED, Instant.now());

        // then
        StepVerifier.create(result).expectNext(updatedPrescription).verifyComplete();
        Mockito.verify(prescriptionEntityRepository).findById(eq(id));
        Mockito.verify(prescriptionEntityRepository).save(any(PrescriptionEntity.class));
        Mockito.verify(mapper).toDomain(eq(entity));
    }

    @Test
    void shouldSavePrescription() {
        // given
        Prescription prescription = Prescription.builder()
                .id(UUID.randomUUID())
                .drugs(List.of(PrescriptionDrug.builder().id(UUID.randomUUID()).build()))
                .build();
        PrescriptionEntity entity = new PrescriptionEntity();
        List<PrescriptionDrugEntity> drugEntities = List.of(new PrescriptionDrugEntity());

        Mockito.when(mapper.toEntity(eq(prescription))).thenReturn(entity);
        Mockito.when(prescriptionEntityRepository.save(eq(entity))).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toEntityList(any())).thenReturn(drugEntities);
        Mockito.when(prescriptionDrugEntityRepository.saveAll((Iterable<PrescriptionDrugEntity>) any()))
                .thenReturn(Flux.fromIterable(drugEntities));
        Mockito.when(mapper.toDomain(eq(entity), any())).thenReturn(prescription);

        // when
        Mono<Prescription> result = adapter.savePrescription(prescription);

        // then
        StepVerifier.create(result).expectNext(prescription).verifyComplete();
        Mockito.verify(mapper).toEntity(eq(prescription));
        Mockito.verify(prescriptionEntityRepository).save(eq(entity));
        Mockito.verify(mapper).toEntityList(any());
        Mockito.verify(prescriptionDrugEntityRepository).saveAll((Iterable<PrescriptionDrugEntity>) any());
        Mockito.verify(mapper).toDomain(eq(entity), any());
    }
}
