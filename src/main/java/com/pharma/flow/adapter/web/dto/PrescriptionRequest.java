package com.pharma.flow.adapter.web.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class PrescriptionRequest {
    private UUID patientId;
    private UUID pharmacyId;
    private List<PrescriptionDrugRequest> drugs;
}
