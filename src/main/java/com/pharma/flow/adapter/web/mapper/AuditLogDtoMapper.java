package com.pharma.flow.adapter.web.mapper;

import com.pharma.flow.adapter.web.dto.AuditLogResponse;
import com.pharma.flow.domain.model.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogDtoMapper {

    AuditLogResponse toResponse(AuditLog domain);
}
