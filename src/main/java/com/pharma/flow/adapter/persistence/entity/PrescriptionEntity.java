package com.pharma.flow.adapter.persistence.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.*;

@Table(name = "prescription")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PrescriptionEntity extends BaseEntity {

    @Column("patient_id")
    private UUID patientId;

    @Column("pharmacy_id")
    private UUID pharmacyId;

    @Column
    private String status;

    @Column("fulfilled_at")
    private Instant fulfilledAt;
}
