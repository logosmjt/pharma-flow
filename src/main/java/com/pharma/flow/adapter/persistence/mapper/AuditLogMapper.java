package com.pharma.flow.adapter.persistence.mapper;

import com.pharma.flow.adapter.persistence.entity.AuditLogEntity;
import com.pharma.flow.domain.model.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLog toDomain(AuditLogEntity entity);

    AuditLogEntity toEntity(AuditLog domain);
}
