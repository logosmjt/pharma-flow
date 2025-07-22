package com.pharma.flow.adapter.web.controller;

import com.pharma.flow.adapter.web.adapter.DrugWebAdapter;
import com.pharma.flow.adapter.web.dto.AddDrugRequest;
import com.pharma.flow.adapter.web.dto.DrugResponse;
import com.pharma.flow.adapter.web.dto.UpdateStockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/drugs")
@RequiredArgsConstructor
@Tag(name = "Drugs", description = "APIs for managing drugs and their inventory")
public class DrugController {

    private final DrugWebAdapter adapter;

    @PostMapping
    @Operation(summary = "Add a new drug", description = "Adds a new drug with batch info and stock quantity")
    public Mono<DrugResponse> createDrug(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Drug information to be added")
                    @RequestBody
                    AddDrugRequest request) {
        return adapter.createDrug(request);
    }

    @PutMapping("/{id}/stock")
    @Operation(
            summary = "Update stock for a drug",
            description = "Updates the stock quantity for a specific drug by ID")
    public Mono<DrugResponse> updateStock(
            @Parameter(description = "Drug ID", required = true) @PathVariable("id") UUID drugId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New stock quantity to update")
                    @RequestBody
                    UpdateStockRequest request) {
        return adapter.updateStock(drugId, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a drug by ID", description = "Fetches the details of a specific drug using its UUID")
    public Mono<DrugResponse> getDrug(@Parameter(description = "Drug ID", required = true) @PathVariable UUID id) {
        return adapter.getDrug(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search drugs by name",
            description = "Search for valid (non-expired) drugs by partial name match")
    public Flux<DrugResponse> searchValidDrugsByName(
            @Parameter(description = "Drug name or partial keyword to search", required = true) @RequestParam
                    String name) {
        return adapter.findValidDrugsByName(name);
    }
}
