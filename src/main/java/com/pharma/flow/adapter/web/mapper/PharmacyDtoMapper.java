package com.pharma.flow.adapter.web.mapper;

import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationRequest;
import com.pharma.flow.adapter.web.dto.PharmacyDrugAllocationResponse;
import com.pharma.flow.adapter.web.dto.PharmacyRequest;
import com.pharma.flow.adapter.web.dto.PharmacyResponse;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PharmacyDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "allocations", ignore = true)
    Pharmacy toDomain(PharmacyRequest request);

    PharmacyResponse toResponse(Pharmacy pharmacy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PharmacyDrugAllocation toDomain(PharmacyDrugAllocationRequest request);

    PharmacyDrugAllocationResponse toResponse(PharmacyDrugAllocation allocation);

    List<PharmacyDrugAllocationResponse> toResponseList(List<PharmacyDrugAllocation> allocations);
}
