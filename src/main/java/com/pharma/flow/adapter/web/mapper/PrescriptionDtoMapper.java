package com.pharma.flow.adapter.web.mapper;

import com.pharma.flow.adapter.web.dto.PrescriptionDrugRequest;
import com.pharma.flow.adapter.web.dto.PrescriptionDrugResponse;
import com.pharma.flow.adapter.web.dto.PrescriptionRequest;
import com.pharma.flow.adapter.web.dto.PrescriptionResponse;
import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionDrug;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "fulfilledAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prescription toDomain(PrescriptionRequest request);

    List<PrescriptionDrug> toDomain(List<PrescriptionDrugRequest> requests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PrescriptionDrug toDomain(PrescriptionDrugRequest request);

    PrescriptionResponse toResponse(Prescription prescription);

    List<PrescriptionDrugResponse> toResponse(List<PrescriptionDrug> drugs);

    PrescriptionDrugResponse toResponse(PrescriptionDrug drug);
}
