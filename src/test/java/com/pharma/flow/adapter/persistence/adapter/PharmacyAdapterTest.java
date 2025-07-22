package com.pharma.flow.adapter.persistence.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.persistence.entity.DrugEntity;
import com.pharma.flow.adapter.persistence.entity.PharmacyDrugAllocationEntity;
import com.pharma.flow.adapter.persistence.entity.PharmacyEntity;
import com.pharma.flow.adapter.persistence.mapper.PharmacyMapper;
import com.pharma.flow.adapter.persistence.repository.DrugEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PharmacyDrugAllocationEntityRepository;
import com.pharma.flow.adapter.persistence.repository.PharmacyEntityRepository;
import com.pharma.flow.adapter.web.exception.UnprocessableException;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PharmacyAdapterTest {

    private final PharmacyEntityRepository pharmacyEntityRepository = Mockito.mock(PharmacyEntityRepository.class);
    private final PharmacyDrugAllocationEntityRepository allocationRepository =
            Mockito.mock(PharmacyDrugAllocationEntityRepository.class);
    private final DrugEntityRepository drugRepository = Mockito.mock(DrugEntityRepository.class);
    private final PharmacyMapper mapper = Mockito.mock(PharmacyMapper.class);
    private final PharmacyAdapter adapter =
            new PharmacyAdapter(pharmacyEntityRepository, allocationRepository, drugRepository, mapper);

    @Test
    void shouldFindPharmacyById() {
        UUID pharmacyId = UUID.randomUUID();
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        List<PharmacyDrugAllocationEntity> allocationEntities = List.of(new PharmacyDrugAllocationEntity());
        Pharmacy pharmacy = Pharmacy.builder().id(pharmacyId).build();

        Mockito.when(pharmacyEntityRepository.findById(eq(pharmacyId))).thenReturn(Mono.just(pharmacyEntity));
        Mockito.when(allocationRepository.findByPharmacyId(eq(pharmacyId)))
                .thenReturn(Flux.fromIterable(allocationEntities));
        Mockito.when(mapper.toDomain(eq(pharmacyEntity), any())).thenReturn(pharmacy);

        Mono<Pharmacy> result = adapter.findById(pharmacyId);

        StepVerifier.create(result).expectNext(pharmacy).verifyComplete();
        Mockito.verify(pharmacyEntityRepository).findById(eq(pharmacyId));
        Mockito.verify(allocationRepository).findByPharmacyId(eq(pharmacyId));
        Mockito.verify(mapper).toDomain(eq(pharmacyEntity), any());
    }

    @Test
    void shouldFindAllPharmacies() {
        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        List<PharmacyDrugAllocationEntity> allocationEntities = List.of(new PharmacyDrugAllocationEntity());
        Pharmacy pharmacy = Pharmacy.builder().build();

        Mockito.when(pharmacyEntityRepository.findAll()).thenReturn(Flux.just(pharmacyEntity));
        Mockito.when(allocationRepository.findByPharmacyId(any())).thenReturn(Flux.fromIterable(allocationEntities));
        Mockito.when(mapper.toDomain(eq(pharmacyEntity), any())).thenReturn(pharmacy);

        Flux<Pharmacy> result = adapter.findAll();

        StepVerifier.create(result).expectNext(pharmacy).verifyComplete();
        Mockito.verify(pharmacyEntityRepository).findAll();
        Mockito.verify(allocationRepository).findByPharmacyId(any());
        Mockito.verify(mapper).toDomain(eq(pharmacyEntity), any());
    }

    @Test
    void shouldSavePharmacy() {
        Pharmacy pharmacy = Pharmacy.builder().build();
        PharmacyEntity pharmacyEntity = new PharmacyEntity();

        Mockito.when(mapper.toEntity(eq(pharmacy))).thenReturn(pharmacyEntity);
        Mockito.when(pharmacyEntityRepository.save(eq(pharmacyEntity))).thenReturn(Mono.just(pharmacyEntity));
        Mockito.when(mapper.toDomainWithOutAllocation(eq(pharmacyEntity))).thenReturn(pharmacy);

        Mono<Pharmacy> result = adapter.savePharmacy(pharmacy);

        StepVerifier.create(result).expectNext(pharmacy).verifyComplete();
        Mockito.verify(mapper).toEntity(eq(pharmacy));
        Mockito.verify(pharmacyEntityRepository).save(eq(pharmacyEntity));
        Mockito.verify(mapper).toDomainWithOutAllocation(eq(pharmacyEntity));
    }

    @Test
    void shouldSaveAllocation() {
        PharmacyDrugAllocation allocation = PharmacyDrugAllocation.builder()
                .id(UUID.randomUUID())
                .drugId(UUID.randomUUID())
                .allocationLimit(50)
                .build();
        PharmacyDrugAllocationEntity allocationEntity = new PharmacyDrugAllocationEntity();

        DrugEntity drugEntity = new DrugEntity();
        drugEntity.setStock(100);

        Mockito.when(drugRepository.findById(eq(allocation.getDrugId()))).thenReturn(Mono.just(drugEntity));
        Mockito.when(allocationRepository.sumAllocationByDrugId(eq(allocation.getDrugId())))
                .thenReturn(Mono.just(30));
        Mockito.when(allocationRepository.findById(eq(allocation.getId()))).thenReturn(Mono.just(allocationEntity));
        Mockito.when(mapper.toEntity(eq(allocation))).thenReturn(allocationEntity);
        Mockito.when(allocationRepository.save(eq(allocationEntity))).thenReturn(Mono.just(allocationEntity));
        Mockito.when(mapper.toDomain(eq(allocationEntity))).thenReturn(allocation);

        Mono<PharmacyDrugAllocation> result = adapter.saveAllocation(allocation);

        StepVerifier.create(result).expectNext(allocation).verifyComplete();
        Mockito.verify(drugRepository).findById(eq(allocation.getDrugId()));
        Mockito.verify(allocationRepository).sumAllocationByDrugId(eq(allocation.getDrugId()));
        Mockito.verify(allocationRepository).findById(eq(allocation.getId()));
        Mockito.verify(allocationRepository).save(eq(allocationEntity));
        Mockito.verify(mapper).toEntity(eq(allocation));
        Mockito.verify(mapper).toDomain(eq(allocationEntity));
    }

    @Test
    void shouldThrowErrorWhenAllocationExceedsStock() {
        PharmacyDrugAllocation allocation = PharmacyDrugAllocation.builder()
                .drugId(UUID.randomUUID())
                .allocationLimit(100)
                .build();

        Mockito.when(drugRepository.findById(eq(allocation.getDrugId()))).thenReturn(Mono.just(new DrugEntity()));
        Mockito.when(allocationRepository.sumAllocationByDrugId(eq(allocation.getDrugId())))
                .thenReturn(Mono.just(90));

        Mono<PharmacyDrugAllocation> result = adapter.saveAllocation(allocation);

        StepVerifier.create(result).expectError(UnprocessableException.class).verify();
        Mockito.verify(drugRepository).findById(eq(allocation.getDrugId()));
        Mockito.verify(allocationRepository).sumAllocationByDrugId(eq(allocation.getDrugId()));
    }
}
