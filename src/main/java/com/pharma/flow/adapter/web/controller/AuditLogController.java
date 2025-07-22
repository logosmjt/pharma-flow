package com.pharma.flow.adapter.web.controller;

import com.pharma.flow.adapter.web.dto.AuditLogResponse;
import com.pharma.flow.adapter.web.mapper.AuditLogDtoMapper;
import com.pharma.flow.domain.repository.AuditLogRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogDtoMapper mapper;

    @GetMapping("/{id}")
    public Mono<AuditLogResponse> getById(@PathVariable UUID id) {
        return auditLogRepository.findById(id).map(mapper::toResponse);
    }

    @GetMapping("/by-prescription/{prescriptionId}")
    public Flux<AuditLogResponse> getByPrescriptionId(@PathVariable UUID prescriptionId) {
        return auditLogRepository.findByPrescriptionId(prescriptionId).map(mapper::toResponse);
    }

    @GetMapping("/by-pharmacy/{pharmacyId}")
    public Flux<AuditLogResponse> getByPharmacyId(@PathVariable UUID pharmacyId) {
        return auditLogRepository.findByPharmacyId(pharmacyId).map(mapper::toResponse);
    }
}
