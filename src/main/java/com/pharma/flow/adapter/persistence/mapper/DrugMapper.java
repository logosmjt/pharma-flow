package com.pharma.flow.adapter.persistence.mapper;

import com.pharma.flow.adapter.persistence.entity.DrugEntity;
import com.pharma.flow.domain.model.Drug;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DrugMapper {

    Drug toDomain(DrugEntity entity);

    DrugEntity toEntity(Drug drug);
}
