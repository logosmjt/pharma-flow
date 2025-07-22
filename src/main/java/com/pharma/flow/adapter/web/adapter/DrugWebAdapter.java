package com.pharma.flow.adapter.web.adapter;

import com.pharma.flow.adapter.web.dto.AddDrugRequest;
import com.pharma.flow.adapter.web.dto.DrugResponse;
import com.pharma.flow.adapter.web.dto.UpdateStockRequest;
import com.pharma.flow.adapter.web.mapper.DrugDtoMapper;
import com.pharma.flow.application.service.DrugService;
import com.pharma.flow.domain.model.Drug;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DrugWebAdapter {

    private final DrugService drugService;
    private final DrugDtoMapper mapper;

    public Mono<DrugResponse> createDrug(AddDrugRequest request) {
        Drug drug = mapper.toDomain(request);
        return drugService.createDrug(drug).map(mapper::toResponse);
    }

    public Mono<DrugResponse> updateStock(UUID drugId, UpdateStockRequest request) {
        return drugService.updateDrugStock(drugId, request.getStock()).map(mapper::toResponse);
    }

    public Mono<DrugResponse> getDrug(UUID id) {
        return drugService.findById(id).map(mapper::toResponse);
    }

    public Flux<DrugResponse> findValidDrugsByName(String name) {
        return drugService.findValidDrugsByName(name).map(mapper::toResponse);
    }
}
