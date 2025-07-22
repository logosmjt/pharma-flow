package com.pharma.flow.adapter.persistence.entity;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.*;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "pharmacy_drug_allocation")
public class PharmacyDrugAllocationEntity extends BaseEntity {

    @Column("pharmacy_id")
    private UUID pharmacyId;

    @Column("drug_id")
    private UUID drugId;

    @Column("allocation_limit")
    private int allocationLimit;
}
