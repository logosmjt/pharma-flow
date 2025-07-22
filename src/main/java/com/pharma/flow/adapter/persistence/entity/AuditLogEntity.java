package com.pharma.flow.adapter.persistence.entity;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.*;

@Table(name = "audit_log")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AuditLogEntity extends BaseEntity {

    @Column("prescription_id")
    private UUID prescriptionId;

    @Column("patient_id")
    private UUID patientId;

    @Column("pharmacy_id")
    private UUID pharmacyId;

    @Column("requested_drugs")
    private String requestedDrugs;

    @Column("dispensed_drugs")
    private String dispensedDrugs;

    @Column
    private boolean success;

    @Column("failure_reason")
    private String failureReason;
}
