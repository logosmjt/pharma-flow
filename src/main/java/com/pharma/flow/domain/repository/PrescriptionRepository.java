package com.pharma.flow.domain.repository;

import com.pharma.flow.domain.model.Prescription;
import com.pharma.flow.domain.model.PrescriptionStatus;
import java.time.Instant;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface PrescriptionRepository {
    Mono<Prescription> findById(UUID id);

    Mono<Prescription> updateStatus(UUID id, PrescriptionStatus status, Instant fulfilledAt);

    Mono<Prescription> savePrescription(Prescription prescription);
}
