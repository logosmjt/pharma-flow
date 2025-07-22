package com.pharma.flow.adapter.persistence.mapper;

import com.pharma.flow.adapter.persistence.entity.PrescriptionDrugEntity;
import com.pharma.flow.adapter.persistence.entity.PrescriptionEntity;
import com.pharma.flow.domain.model.AuditLog;
import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionDrug;
import com.pharma.flow.domain.model.PrescriptionStatus;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    PrescriptionEntity toEntity(Prescription prescription);

    @Mapping(target = "drugs", ignore = true)
    Prescription toDomain(PrescriptionEntity prescriptionEntity);

    PrescriptionDrug toDomain(PrescriptionDrugEntity entity);

    PrescriptionDrugEntity toEntity(PrescriptionDrug drug);

    List<PrescriptionDrug> toDomainList(List<PrescriptionDrugEntity> entities);

    List<PrescriptionDrugEntity> toEntityList(List<PrescriptionDrug> drugs);

    default Prescription toDomain(PrescriptionEntity entity, List<PrescriptionDrugEntity> drugEntities) {
        if (entity != null && drugEntities != null) {
            List<PrescriptionDrug> drugs =
                    drugEntities.stream().map(this::toDomain).collect(Collectors.toList());
            // Assuming Prescription has a setter or builder for drugs
            return Prescription.builder()
                    .id(entity.getId())
                    .patientId(entity.getPatientId())
                    .pharmacyId(entity.getPharmacyId())
                    .status(PrescriptionStatus.valueOf(entity.getStatus()))
                    .fulfilledAt(entity.getFulfilledAt())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .drugs(drugs)
                    .build();
        }
        return null;
    }

    default AuditLog buildAuditLog(Prescription prescription, boolean success, String failureReason) {
        return AuditLog.builder()
                .prescriptionId(prescription.getId())
                .patientId(prescription.getPatientId())
                .pharmacyId(prescription.getPharmacyId())
                .requestedDrugs(prescription.getDrugs().toString())
                .dispensedDrugs(success ? prescription.getDrugs().toString() : null)
                .success(success)
                .failureReason(failureReason)
                .build();
    }
}
