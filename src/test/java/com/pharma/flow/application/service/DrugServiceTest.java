package com.pharma.flow.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.domain.model.Drug;
import com.pharma.flow.domain.repository.DrugRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DrugServiceTest {

    private final DrugRepository drugRepository = Mockito.mock(DrugRepository.class);
    private final DrugService drugService = new DrugService(drugRepository);

    @Test
    void shouldCreateDrugWhenValidDrugProvided() {
        // given
        Drug drug = new Drug(UUID.randomUUID(), "Drug1", "Manufacturer1", "Batch1", null, 100, null, null);
        Mockito.when(drugRepository.save(any(Drug.class))).thenReturn(Mono.just(drug));

        // when
        Mono<Drug> result = drugService.createDrug(drug);

        // then
        StepVerifier.create(result).expectNext(drug).verifyComplete();
        Mockito.verify(drugRepository).save(eq(drug));
    }

    @Test
    void shouldUpdateDrugStockWhenDrugExists() {
        // given
        UUID drugId = UUID.randomUUID();
        Drug existingDrug = new Drug(drugId, "Drug1", "Manufacturer1", "Batch1", null, 100, null, null);
        Drug updatedDrug = new Drug(drugId, "Drug1", "Manufacturer1", "Batch1", null, 200, null, null);
        Mockito.when(drugRepository.findById(eq(drugId))).thenReturn(Mono.just(existingDrug));
        Mockito.when(drugRepository.save(any(Drug.class))).thenReturn(Mono.just(updatedDrug));

        // when
        Mono<Drug> result = drugService.updateDrugStock(drugId, 200);

        // then
        StepVerifier.create(result).expectNext(updatedDrug).verifyComplete();
        Mockito.verify(drugRepository).findById(eq(drugId));
        Mockito.verify(drugRepository).save(eq(updatedDrug));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentDrugStock() {
        // given
        UUID drugId = UUID.randomUUID();
        Mockito.when(drugRepository.findById(eq(drugId))).thenReturn(Mono.empty());

        // when
        Mono<Drug> result = drugService.updateDrugStock(drugId, 200);

        // then
        StepVerifier.create(result).expectError(NotFoundException.class).verify();
        Mockito.verify(drugRepository).findById(eq(drugId));
        Mockito.verify(drugRepository, Mockito.never()).save(any(Drug.class));
    }

    @Test
    void shouldFindDrugByIdWhenDrugExists() {
        // given
        UUID drugId = UUID.randomUUID();
        Drug drug = new Drug(drugId, "Drug1", "Manufacturer1", "Batch1", null, 100, null, null);
        Mockito.when(drugRepository.findById(eq(drugId))).thenReturn(Mono.just(drug));

        // when
        Mono<Drug> result = drugService.findById(drugId);

        // then
        StepVerifier.create(result).expectNext(drug).verifyComplete();
        Mockito.verify(drugRepository).findById(eq(drugId));
    }

    @Test
    void shouldFindValidDrugsByNameWhenDrugsExist() {
        // given
        String name = "Drug1";
        Drug drug1 = new Drug(UUID.randomUUID(), name, "Manufacturer1", "Batch1", null, 100, null, null);
        Drug drug2 = new Drug(UUID.randomUUID(), name, "Manufacturer2", "Batch2", null, 200, null, null);
        Mockito.when(drugRepository.findAllValidByName(eq(name))).thenReturn(Flux.just(drug1, drug2));

        // when
        Flux<Drug> result = drugService.findValidDrugsByName(name);

        // then
        StepVerifier.create(result).expectNext(drug1, drug2).verifyComplete();
        Mockito.verify(drugRepository).findAllValidByName(eq(name));
    }
}
