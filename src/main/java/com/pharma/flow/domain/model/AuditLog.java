package com.pharma.flow.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AuditLog {
    UUID id;
    UUID prescriptionId;
    UUID patientId;
    UUID pharmacyId;
    String requestedDrugs;
    String dispensedDrugs;
    boolean success;
    String failureReason;
    Instant createdAt;
    Instant updatedAt;
}
