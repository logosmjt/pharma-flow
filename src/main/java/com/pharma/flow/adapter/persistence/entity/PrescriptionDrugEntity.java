package com.pharma.flow.adapter.persistence.entity;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.*;

@Table(name = "prescription_drug")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PrescriptionDrugEntity extends BaseEntity {

    @Column("prescription_id")
    private UUID prescriptionId;

    @Column("drug_id")
    private UUID drugId;

    @Column
    private int quantity;
}
