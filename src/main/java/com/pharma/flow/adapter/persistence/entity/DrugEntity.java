package com.pharma.flow.adapter.persistence.entity;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Table(name = "drug")
public class DrugEntity extends BaseEntity {

    @Column
    private String name;

    @Column
    private String manufacturer;

    @Column("batch_number")
    private String batchNumber;

    @Column("expiry_date")
    private LocalDate expiryDate;

    @Column
    private int stock;
}
