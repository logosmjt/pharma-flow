package com.pharma.flow.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Prescription {
    UUID id;
    UUID patientId;
    UUID pharmacyId;
    PrescriptionStatus status;
    Instant fulfilledAt;
    Instant createdAt;
    Instant updatedAt;
    List<PrescriptionDrug> drugs;
}
