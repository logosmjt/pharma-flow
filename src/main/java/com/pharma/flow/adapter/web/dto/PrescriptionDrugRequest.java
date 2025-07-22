package com.pharma.flow.adapter.web.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class PrescriptionDrugRequest {
    private UUID drugId;
    private int quantity;
}
