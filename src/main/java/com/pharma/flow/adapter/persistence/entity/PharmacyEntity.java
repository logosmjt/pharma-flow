package com.pharma.flow.adapter.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.*;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "pharmacy")
public class PharmacyEntity extends BaseEntity {

    @Column
    private String name;

    @Column
    private String location;
}
