package com.pharma.flow.adapter.web.adapter;

import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.web.dto.*;
import com.pharma.flow.adapter.web.mapper.PharmacyDtoMapper;
import com.pharma.flow.application.service.PharmacyService;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PharmacyWebAdapterTest {

    private final PharmacyService pharmacyService = Mockito.mock(PharmacyService.class);
    private final PharmacyDtoMapper mapper = Mockito.mock(PharmacyDtoMapper.class);
    private final PharmacyWebAdapter adapter = new PharmacyWebAdapter(pharmacyService, mapper);

    @Test
    void shouldFindPharmacyById() {
        UUID id = UUID.randomUUID();
        Pharmacy pharmacy = Pharmacy.builder()
                .id(id)
                .name("Pharmacy A")
                .location("Location A")
                .build();
        PharmacyResponse response = new PharmacyResponse();
        response.setId(id);
        response.setName("Pharmacy A");
        response.setLocation("Location A");

        Mockito.when(pharmacyService.findById(eq(id))).thenReturn(Mono.just(pharmacy));
        Mockito.when(mapper.toResponse(eq(pharmacy))).thenReturn(response);

        Mono<PharmacyResponse> result = adapter.findById(id);

        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(pharmacyService).findById(eq(id));
        Mockito.verify(mapper).toResponse(eq(pharmacy));
    }

    @Test
    void shouldCreatePharmacy() {
        PharmacyRequest request = new PharmacyRequest();
        request.setName("Pharmacy A");
        request.setLocation("Location A");

        Pharmacy pharmacy =
                Pharmacy.builder().name("Pharmacy A").location("Location A").build();
        PharmacyResponse response = new PharmacyResponse();
        response.setName("Pharmacy A");
        response.setLocation("Location A");

        Mockito.when(mapper.toDomain(eq(request))).thenReturn(pharmacy);
        Mockito.when(pharmacyService.createPharmacy(eq(pharmacy))).thenReturn(Mono.just(pharmacy));
        Mockito.when(mapper.toResponse(eq(pharmacy))).thenReturn(response);

        Mono<PharmacyResponse> result = adapter.createPharmacy(request);

        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(mapper).toDomain(eq(request));
        Mockito.verify(pharmacyService).createPharmacy(eq(pharmacy));
        Mockito.verify(mapper).toResponse(eq(pharmacy));
    }

    @Test
    void shouldListPharmacies() {
        Pharmacy pharmacy = Pharmacy.builder()
                .id(UUID.randomUUID())
                .name("Pharmacy A")
                .location("Location A")
                .build();
        PharmacyResponse response = new PharmacyResponse();
        response.setId(pharmacy.getId());
        response.setName("Pharmacy A");
        response.setLocation("Location A");

        Mockito.when(pharmacyService.listPharmacies()).thenReturn(Flux.just(pharmacy));
        Mockito.when(mapper.toResponse(eq(pharmacy))).thenReturn(response);

        Flux<PharmacyResponse> result = adapter.listPharmacies();

        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(pharmacyService).listPharmacies();
        Mockito.verify(mapper).toResponse(eq(pharmacy));
    }

    @Test
    void shouldCreateAllocation() {
        PharmacyDrugAllocationRequest request = new PharmacyDrugAllocationRequest();
        request.setPharmacyId(UUID.randomUUID());
        request.setDrugId(UUID.randomUUID());
        request.setAllocationLimit(100);

        PharmacyDrugAllocation allocation = PharmacyDrugAllocation.builder()
                .pharmacyId(request.getPharmacyId())
                .drugId(request.getDrugId())
                .allocationLimit(request.getAllocationLimit())
                .build();

        PharmacyDrugAllocationResponse response = new PharmacyDrugAllocationResponse();
        response.setPharmacyId(request.getPharmacyId());
        response.setDrugId(request.getDrugId());
        response.setAllocationLimit(request.getAllocationLimit());

        Mockito.when(mapper.toDomain(eq(request))).thenReturn(allocation);
        Mockito.when(pharmacyService.createAllocation(eq(allocation))).thenReturn(Mono.just(allocation));
        Mockito.when(mapper.toResponse(eq(allocation))).thenReturn(response);

        Mono<PharmacyDrugAllocationResponse> result = adapter.createAllocation(request);

        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(mapper).toDomain(eq(request));
        Mockito.verify(pharmacyService).createAllocation(eq(allocation));
        Mockito.verify(mapper).toResponse(eq(allocation));
    }

    @Test
    void shouldUpdateAllocationLimit() {
        UUID allocationId = UUID.randomUUID();
        UpdateAllocationLimitRequest request = new UpdateAllocationLimitRequest();
        request.setAllocationLimit(200);

        PharmacyDrugAllocation allocation = PharmacyDrugAllocation.builder()
                .id(allocationId)
                .allocationLimit(request.getAllocationLimit())
                .build();

        PharmacyDrugAllocationResponse response = new PharmacyDrugAllocationResponse();
        response.setId(allocationId);
        response.setAllocationLimit(request.getAllocationLimit());

        Mockito.when(pharmacyService.updateAllocationLimit(eq(allocationId), eq(request.getAllocationLimit())))
                .thenReturn(Mono.just(allocation));
        Mockito.when(mapper.toResponse(eq(allocation))).thenReturn(response);

        Mono<PharmacyDrugAllocationResponse> result = adapter.updateAllocationLimit(allocationId, request);

        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(pharmacyService).updateAllocationLimit(eq(allocationId), eq(request.getAllocationLimit()));
        Mockito.verify(mapper).toResponse(eq(allocation));
    }
}
