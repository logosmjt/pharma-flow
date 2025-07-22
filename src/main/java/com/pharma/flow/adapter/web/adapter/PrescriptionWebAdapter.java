package com.pharma.flow.adapter.web.adapter;

import com.pharma.flow.adapter.web.dto.PrescriptionRequest;
import com.pharma.flow.adapter.web.dto.PrescriptionResponse;
import com.pharma.flow.adapter.web.mapper.PrescriptionDtoMapper;
import com.pharma.flow.application.service.PrescriptionService;
import com.pharma.flow.domain.model.Prescription;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PrescriptionWebAdapter {

    private final PrescriptionService prescriptionService;
    private final PrescriptionDtoMapper mapper;

    public Mono<PrescriptionResponse> createPrescription(PrescriptionRequest request) {
        Prescription prescription = mapper.toDomain(request);
        return prescriptionService.savePrescription(prescription).map(mapper::toResponse);
    }

    public Mono<PrescriptionResponse> fulfillment(UUID id) {
        return prescriptionService.fulfillment(id).map(mapper::toResponse);
    }

    public Mono<PrescriptionResponse> findById(UUID id) {
        return prescriptionService.getPrescriptionById(id).map(mapper::toResponse);
    }
}
