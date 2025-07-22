package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.PrescriptionDrugEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PrescriptionDrugEntityRepository extends ReactiveCrudRepository<PrescriptionDrugEntity, UUID> {
    Flux<PrescriptionDrugEntity> findByPrescriptionId(UUID prescriptionId);
}
