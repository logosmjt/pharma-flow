package com.pharma.flow.adapter.persistence.mapper;

import com.pharma.flow.adapter.persistence.entity.PharmacyDrugAllocationEntity;
import com.pharma.flow.adapter.persistence.entity.PharmacyEntity;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {

    default Pharmacy toDomain(PharmacyEntity entity, List<PharmacyDrugAllocationEntity> allocations) {
        if (entity == null) {
            return null;
        }
        return Pharmacy.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .allocations(toDomainList(allocations))
                .build();
    }

    @Mapping(target = "allocations", ignore = true)
    Pharmacy toDomainWithOutAllocation(PharmacyEntity entity);

    PharmacyEntity toEntity(Pharmacy domain);

    PharmacyDrugAllocation toDomain(PharmacyDrugAllocationEntity entity);

    PharmacyDrugAllocationEntity toEntity(PharmacyDrugAllocation domain);

    List<PharmacyDrugAllocation> toDomainList(List<PharmacyDrugAllocationEntity> entities);

    List<PharmacyDrugAllocationEntity> toEntityList(List<PharmacyDrugAllocation> domains);
}
