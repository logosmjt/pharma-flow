package com.pharma.flow.adapter.web.controller;

import com.pharma.flow.adapter.web.adapter.PrescriptionWebAdapter;
import com.pharma.flow.adapter.web.dto.PrescriptionRequest;
import com.pharma.flow.adapter.web.dto.PrescriptionResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionWebAdapter adapter;

    @PostMapping
    public Mono<PrescriptionResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        return adapter.createPrescription(request);
    }

    @PostMapping("/{id}/fulfillment")
    public Mono<PrescriptionResponse> fulfillment(@PathVariable UUID id) {
        return adapter.fulfillment(id);
    }

    @GetMapping("/{id}")
    public Mono<PrescriptionResponse> findById(@PathVariable UUID id) {
        return adapter.findById(id);
    }
}
