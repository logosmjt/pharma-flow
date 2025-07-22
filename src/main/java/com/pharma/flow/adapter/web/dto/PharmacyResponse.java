package com.pharma.flow.adapter.web.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class PharmacyResponse {
    private UUID id;
    private String name;
    private String location;
    private List<PharmacyDrugAllocationResponse> allocations;
}
