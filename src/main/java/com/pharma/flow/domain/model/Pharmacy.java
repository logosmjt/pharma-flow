package com.pharma.flow.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pharmacy {
    private UUID id;
    private String name;
    private String location;
    private Instant createdAt;
    private Instant updatedAt;
    private List<PharmacyDrugAllocation> allocations;
}
