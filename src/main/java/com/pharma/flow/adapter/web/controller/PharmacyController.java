package com.pharma.flow.adapter.web.controller;

import com.pharma.flow.adapter.web.adapter.PharmacyWebAdapter;
import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationRequest;
import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationResponse;
import com.pharma.flow.adapter.web.dto.PharmacyRequest;
import com.pharma.flow.adapter.web.dto.PharmacyResponse;
import com.pharma.flow.adapter.web.dto.UpdateAllocationLimitRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyWebAdapter adapter;

    @GetMapping("/{id}")
    public Mono<PharmacyResponse> findById(@PathVariable UUID id) {
        return adapter.findById(id);
    }

    @GetMapping
    public Flux<PharmacyResponse> listPharmacies() {
        return adapter.listPharmacies();
    }

    @PostMapping
    public Mono<PharmacyResponse> createPharmacy(@RequestBody PharmacyRequest request) {
        return adapter.createPharmacy(request);
    }

    @PostMapping("/allocations")
    public Mono<PharmacyDrugAllocationResponse> createAllocation(@RequestBody PharmacyDrugAllocationRequest request) {
        return adapter.createAllocation(request);
    }

    @PatchMapping("/allocations/{id}")
    public Mono<PharmacyDrugAllocationResponse> updateAllocationLimit(
            @PathVariable("id") UUID allocationId, @RequestBody UpdateAllocationLimitRequest request) {
        return adapter.updateAllocationLimit(allocationId, request);
    }
}
