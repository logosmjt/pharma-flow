package com.pharma.flow.adapter.web.adapter;

import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.web.dto.AddDrugRequest;
import com.pharma.flow.adapter.web.dto.DrugResponse;
import com.pharma.flow.adapter.web.dto.UpdateStockRequest;
import com.pharma.flow.adapter.web.mapper.DrugDtoMapper;
import com.pharma.flow.application.service.DrugService;
import com.pharma.flow.domain.model.Drug;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DrugWebAdapterTest {

    private final DrugService drugService = Mockito.mock(DrugService.class);
    private final DrugDtoMapper mapper = Mockito.mock(DrugDtoMapper.class);
    private final DrugWebAdapter adapter = new DrugWebAdapter(drugService, mapper);

    @Test
    void shouldCreateDrug() {
        // given
        AddDrugRequest request = AddDrugRequest.builder()
                .name("Aspirin")
                .manufacturer("Pharma Inc.")
                .batchNumber("B123")
                .expiryDate(LocalDate.now().plusDays(30))
                .stock(100)
                .build();
        Drug drug = Drug.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .batchNumber(request.getBatchNumber())
                .expiryDate(request.getExpiryDate())
                .stock(request.getStock())
                .build();
        DrugResponse response = DrugResponse.builder()
                .id(drug.getId())
                .name(drug.getName())
                .manufacturer(drug.getManufacturer())
                .batchNumber(drug.getBatchNumber())
                .expiryDate(drug.getExpiryDate())
                .stock(drug.getStock())
                .build();

        Mockito.when(mapper.toDomain(eq(request))).thenReturn(drug);
        Mockito.when(drugService.createDrug(eq(drug))).thenReturn(Mono.just(drug));
        Mockito.when(mapper.toResponse(eq(drug))).thenReturn(response);

        // when
        Mono<DrugResponse> result = adapter.createDrug(request);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(mapper).toDomain(eq(request));
        Mockito.verify(drugService).createDrug(eq(drug));
        Mockito.verify(mapper).toResponse(eq(drug));
    }

    @Test
    void shouldUpdateStock() {
        // given
        UUID drugId = UUID.randomUUID();
        UpdateStockRequest request = new UpdateStockRequest(200);
        Drug drug = Drug.builder().id(drugId).stock(request.getStock()).build();
        DrugResponse response =
                DrugResponse.builder().id(drug.getId()).stock(drug.getStock()).build();

        Mockito.when(drugService.updateDrugStock(eq(drugId), eq(request.getStock())))
                .thenReturn(Mono.just(drug));
        Mockito.when(mapper.toResponse(eq(drug))).thenReturn(response);

        // when
        Mono<DrugResponse> result = adapter.updateStock(drugId, request);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(drugService).updateDrugStock(eq(drugId), eq(request.getStock()));
        Mockito.verify(mapper).toResponse(eq(drug));
    }

    @Test
    void shouldGetDrug() {
        // given
        UUID drugId = UUID.randomUUID();
        Drug drug = Drug.builder().id(drugId).name("Aspirin").build();
        DrugResponse response =
                DrugResponse.builder().id(drug.getId()).name(drug.getName()).build();

        Mockito.when(drugService.findById(eq(drugId))).thenReturn(Mono.just(drug));
        Mockito.when(mapper.toResponse(eq(drug))).thenReturn(response);

        // when
        Mono<DrugResponse> result = adapter.getDrug(drugId);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(drugService).findById(eq(drugId));
        Mockito.verify(mapper).toResponse(eq(drug));
    }

    @Test
    void shouldFindValidDrugsByName() {
        // given
        String name = "Aspirin";
        Drug drug = Drug.builder().id(UUID.randomUUID()).name(name).build();
        DrugResponse response =
                DrugResponse.builder().id(drug.getId()).name(drug.getName()).build();

        Mockito.when(drugService.findValidDrugsByName(eq(name))).thenReturn(Flux.just(drug));
        Mockito.when(mapper.toResponse(eq(drug))).thenReturn(response);

        // when
        Flux<DrugResponse> result = adapter.findValidDrugsByName(name);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(drugService).findValidDrugsByName(eq(name));
        Mockito.verify(mapper).toResponse(eq(drug));
    }
}
