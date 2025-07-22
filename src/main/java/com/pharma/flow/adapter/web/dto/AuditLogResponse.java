package com.pharma.flow.adapter.web.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class AuditLogResponse {
    private UUID id;
    private UUID prescriptionId;
    private UUID patientId;
    private UUID pharmacyId;
    private String requestedDrugs;
    private String dispensedDrugs;
    private boolean success;
    private String failureReason;
    private Instant createdAt;
}
