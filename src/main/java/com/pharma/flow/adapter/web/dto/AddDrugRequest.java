package com.pharma.flow.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.*;

@Schema(description = "Request body for adding a new drug with batch and inventory information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddDrugRequest {

    @Schema(description = "Name of the drug", example = "name", required = true)
    private String name;

    @Schema(description = "Name of the manufacturer", example = "manufacturer", required = true)
    private String manufacturer;

    @Schema(description = "Batch number for the drug", example = "BATCH-001", required = true)
    private String batchNumber;

    @Schema(description = "Expiry date of the drug", example = "2026-12-31", required = true)
    private LocalDate expiryDate;

    @Schema(description = "Initial stock quantity", example = "100", required = true)
    private int stock;
}
