package com.pharma.flow.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PrescriptionDrug {
    UUID id;
    UUID prescriptionId;
    UUID drugId;
    int quantity;
    Instant createdAt;
    Instant updatedAt;
}
