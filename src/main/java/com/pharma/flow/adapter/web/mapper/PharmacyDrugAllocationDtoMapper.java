package com.pharma.flow.adapter.web.mapper;

import com.pharma.flow.adapter.web.dto.AllocateDrugRequest;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PharmacyDrugAllocationDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PharmacyDrugAllocation toDomain(AllocateDrugRequest request);
}
