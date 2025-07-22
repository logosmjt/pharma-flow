package com.pharma.flow.adapter.web.dto;

import com.pharma.flow.domain.model.PrescriptionStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class PrescriptionResponse {
    private UUID id;
    private UUID patientId;
    private UUID pharmacyId;
    private List<PrescriptionDrugResponse> drugs; // todo fulfillment的时候没有返回drugs
    private PrescriptionStatus status;
    private Instant fulfilledAt;
}
