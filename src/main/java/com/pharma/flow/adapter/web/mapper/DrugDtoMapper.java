package com.pharma.flow.adapter.web.mapper;

import com.pharma.flow.adapter.web.dto.AddDrugRequest;
import com.pharma.flow.adapter.web.dto.DrugResponse;
import com.pharma.flow.domain.model.Drug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrugDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Drug toDomain(AddDrugRequest request);

    DrugResponse toResponse(Drug drug);
}
