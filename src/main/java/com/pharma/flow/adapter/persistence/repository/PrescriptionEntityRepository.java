package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.PrescriptionEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PrescriptionEntityRepository extends ReactiveCrudRepository<PrescriptionEntity, UUID> {}
