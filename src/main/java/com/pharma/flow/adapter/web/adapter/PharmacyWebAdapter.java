package com.pharma.flow.adapter.web.adapter;

import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationRequest;
import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationResponse;
import com.pharma.flow.adapter.web.dto.PharmacyRequest;
import com.pharma.flow.adapter.web.dto.PharmacyResponse;
import com.pharma.flow.adapter.web.dto.UpdateAllocationLimitRequest;
import com.pharma.flow.adapter.web.mapper.PharmacyDtoMapper;
import com.pharma.flow.application.service.PharmacyService;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PharmacyWebAdapter {

    private final PharmacyService pharmacyService;
    private final PharmacyDtoMapper mapper;

    public Mono<PharmacyResponse> findById(UUID id) {
        return pharmacyService.findById(id).map(mapper::toResponse);
    }

    public Mono<PharmacyResponse> createPharmacy(PharmacyRequest request) {
        Pharmacy pharmacy = mapper.toDomain(request);
        return pharmacyService.createPharmacy(pharmacy).map(mapper::toResponse);
    }

    public Flux<PharmacyResponse> listPharmacies() {
        return pharmacyService.listPharmacies().map(mapper::toResponse);
    }

    public Mono<PharmacyDrugAllocationResponse> createAllocation(PharmacyDrugAllocationRequest request) {
        PharmacyDrugAllocation allocation = mapper.toDomain(request);
        log.debug("Saving Pharmacy Allocation Request: {}", allocation);
        return pharmacyService.createAllocation(allocation).map(mapper::toResponse);
    }

    public Mono<PharmacyDrugAllocationResponse> updateAllocationLimit(
            UUID allocationId, UpdateAllocationLimitRequest request) {
        return pharmacyService
                .updateAllocationLimit(allocationId, request.getAllocationLimit())
                .map(mapper::toResponse);
    }
}
