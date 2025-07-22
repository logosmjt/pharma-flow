package com.pharma.flow.adapter.web.adapter;

import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.web.dto.PrescriptionRequest;
import com.pharma.flow.adapter.web.dto.PrescriptionResponse;
import com.pharma.flow.adapter.web.mapper.PrescriptionDtoMapper;
import com.pharma.flow.application.service.PrescriptionService;
import com.pharma.flow.domain.model.Prescription;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PrescriptionWebAdapterTest {

    private final PrescriptionService prescriptionService = Mockito.mock(PrescriptionService.class);
    private final PrescriptionDtoMapper mapper = Mockito.mock(PrescriptionDtoMapper.class);
    private final PrescriptionWebAdapter adapter = new PrescriptionWebAdapter(prescriptionService, mapper);

    @Test
    void shouldCreatePrescription() {
        // given
        PrescriptionRequest request = new PrescriptionRequest();
        Prescription prescription = new Prescription();
        PrescriptionResponse response = new PrescriptionResponse();

        Mockito.when(mapper.toDomain(eq(request))).thenReturn(prescription);
        Mockito.when(prescriptionService.savePrescription(eq(prescription))).thenReturn(Mono.just(prescription));
        Mockito.when(mapper.toResponse(eq(prescription))).thenReturn(response);

        // when
        Mono<PrescriptionResponse> result = adapter.createPrescription(request);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(mapper).toDomain(eq(request));
        Mockito.verify(prescriptionService).savePrescription(eq(prescription));
        Mockito.verify(mapper).toResponse(eq(prescription));
    }

    @Test
    void shouldFulfillPrescription() {
        // given
        UUID id = UUID.randomUUID();
        Prescription prescription = new Prescription();
        PrescriptionResponse response = new PrescriptionResponse();

        Mockito.when(prescriptionService.fulfillment(eq(id))).thenReturn(Mono.just(prescription));
        Mockito.when(mapper.toResponse(eq(prescription))).thenReturn(response);

        // when
        Mono<PrescriptionResponse> result = adapter.fulfillment(id);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(prescriptionService).fulfillment(eq(id));
        Mockito.verify(mapper).toResponse(eq(prescription));
    }

    @Test
    void shouldFindPrescriptionById() {
        // given
        UUID id = UUID.randomUUID();
        Prescription prescription = new Prescription();
        PrescriptionResponse response = new PrescriptionResponse();

        Mockito.when(prescriptionService.getPrescriptionById(eq(id))).thenReturn(Mono.just(prescription));
        Mockito.when(mapper.toResponse(eq(prescription))).thenReturn(response);

        // when
        Mono<PrescriptionResponse> result = adapter.findById(id);

        // then
        StepVerifier.create(result).expectNext(response).verifyComplete();
        Mockito.verify(prescriptionService).getPrescriptionById(eq(id));
        Mockito.verify(mapper).toResponse(eq(prescription));
    }
}
