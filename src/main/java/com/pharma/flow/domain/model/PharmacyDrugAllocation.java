package com.pharma.flow.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PharmacyDrugAllocation {
    private UUID id;
    private UUID pharmacyId;
    private UUID drugId;
    private int allocationLimit;
    private Instant createdAt;
    private Instant updatedAt;
}
