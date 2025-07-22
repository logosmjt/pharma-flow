package com.pharma.flow.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import com.pharma.flow.domain.repository.PharmacyRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PharmacyServiceTest {

    private final PharmacyRepository pharmacyRepository = Mockito.mock(PharmacyRepository.class);
    private final TransactionalOperator tx = Mockito.mock(TransactionalOperator.class);
    private final PharmacyService pharmacyService = new PharmacyService(pharmacyRepository, tx);

    @Test
    void shouldCreateOrUpdatePharmacyWhenValidPharmacyProvided() {
        // given
        Pharmacy pharmacy = new Pharmacy(UUID.randomUUID(), "Pharmacy1", "Location1", null, null, List.of());
        Mockito.when(pharmacyRepository.savePharmacy(any(Pharmacy.class))).thenReturn(Mono.just(pharmacy));
        Mockito.when(tx.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Mono<Pharmacy> result = pharmacyService.createPharmacy(pharmacy);

        // then
        StepVerifier.create(result).expectNext(pharmacy).verifyComplete();
        Mockito.verify(pharmacyRepository).savePharmacy(eq(pharmacy));
    }

    @Test
    void shouldFindPharmacyByIdWhenPharmacyExists() {
        // given
        UUID pharmacyId = UUID.randomUUID();
        Pharmacy pharmacy = new Pharmacy(pharmacyId, "Pharmacy1", "Location1", null, null, List.of());
        Mockito.when(pharmacyRepository.findById(eq(pharmacyId))).thenReturn(Mono.just(pharmacy));

        // when
        Mono<Pharmacy> result = pharmacyService.findById(pharmacyId);

        // then
        StepVerifier.create(result).expectNext(pharmacy).verifyComplete();
        Mockito.verify(pharmacyRepository).findById(eq(pharmacyId));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPharmacyDoesNotExist() {
        // given
        UUID pharmacyId = UUID.randomUUID();
        Mockito.when(pharmacyRepository.findById(eq(pharmacyId))).thenReturn(Mono.empty());

        // when
        Mono<Pharmacy> result = pharmacyService.findById(pharmacyId);

        // then
        StepVerifier.create(result).expectError(NotFoundException.class).verify();
        Mockito.verify(pharmacyRepository).findById(eq(pharmacyId));
    }

    @Test
    void shouldListAllPharmacies() {
        // given
        Pharmacy pharmacy1 = new Pharmacy(UUID.randomUUID(), "Pharmacy1", "Location1", null, null, List.of());
        Pharmacy pharmacy2 = new Pharmacy(UUID.randomUUID(), "Pharmacy2", "Location2", null, null, List.of());
        Mockito.when(pharmacyRepository.findAll()).thenReturn(Flux.just(pharmacy1, pharmacy2));

        // when
        Flux<Pharmacy> result = pharmacyService.listPharmacies();

        // then
        StepVerifier.create(result).expectNext(pharmacy1, pharmacy2).verifyComplete();
        Mockito.verify(pharmacyRepository).findAll();
    }

    @Test
    void shouldCreateAllocationWhenValidAllocationProvided() {
        // given
        PharmacyDrugAllocation allocation =
                new PharmacyDrugAllocation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 100, null, null);
        Mockito.when(pharmacyRepository.saveAllocation(any(PharmacyDrugAllocation.class)))
                .thenReturn(Mono.just(allocation));
        Mockito.when(tx.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Mono<PharmacyDrugAllocation> result = pharmacyService.createAllocation(allocation);

        // then
        StepVerifier.create(result).expectNext(allocation).verifyComplete();
        Mockito.verify(pharmacyRepository).saveAllocation(eq(allocation));
    }

    @Test
    void shouldUpdateAllocationLimitWhenAllocationExists() {
        // given
        UUID allocationId = UUID.randomUUID();
        PharmacyDrugAllocation existingAllocation =
                new PharmacyDrugAllocation(allocationId, UUID.randomUUID(), UUID.randomUUID(), 100, null, null);
        PharmacyDrugAllocation updatedAllocation = new PharmacyDrugAllocation(
                allocationId, existingAllocation.getPharmacyId(), existingAllocation.getDrugId(), 200, null, null);
        Mockito.when(pharmacyRepository.findAllocationById(eq(allocationId))).thenReturn(Mono.just(existingAllocation));
        Mockito.when(pharmacyRepository.saveAllocation(any(PharmacyDrugAllocation.class)))
                .thenReturn(Mono.just(updatedAllocation));

        // when
        Mono<PharmacyDrugAllocation> result = pharmacyService.updateAllocationLimit(allocationId, 200);

        // then
        StepVerifier.create(result).expectNext(updatedAllocation).verifyComplete();
        Mockito.verify(pharmacyRepository).findAllocationById(eq(allocationId));
        Mockito.verify(pharmacyRepository).saveAllocation(eq(updatedAllocation));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentAllocation() {
        // given
        UUID allocationId = UUID.randomUUID();
        Mockito.when(pharmacyRepository.findAllocationById(eq(allocationId))).thenReturn(Mono.empty());

        // when
        Mono<PharmacyDrugAllocation> result = pharmacyService.updateAllocationLimit(allocationId, 200);

        // then
        StepVerifier.create(result).expectError(NotFoundException.class).verify();
        Mockito.verify(pharmacyRepository).findAllocationById(eq(allocationId));
        Mockito.verify(pharmacyRepository, Mockito.never()).saveAllocation(any(PharmacyDrugAllocation.class));
    }
}
