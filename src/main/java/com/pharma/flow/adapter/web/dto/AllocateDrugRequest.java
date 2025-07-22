package com.pharma.flow.adapter.web.dto;

import java.util.UUID;
import lombok.Value;

@Value
public class AllocateDrugRequest {
    UUID pharmacyId;
    UUID drugId;
    int allocationLimit;
}
