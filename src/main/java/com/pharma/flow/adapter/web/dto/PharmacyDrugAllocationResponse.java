package com.pharma.flow.adapter.web.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class PharmacyDrugAllocationResponse {
    private UUID id;
    private UUID pharmacyId;
    private UUID drugId;
    private int allocationLimit;
}
