package com.pharma.flow.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Schema(description = "Response object representing drug details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugResponse {

    @Schema(description = "Unique identifier of the drug", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Name of the drug", example = "name")
    private String name;

    @Schema(description = "Name of the manufacturer", example = "manufacturer")
    private String manufacturer;

    @Schema(description = "Batch number of the drug", example = "BATCH-001")
    private String batchNumber;

    @Schema(description = "Expiry date of the drug", example = "2026-12-31")
    private LocalDate expiryDate;

    @Schema(description = "Current stock available", example = "100")
    private int stock;
}
