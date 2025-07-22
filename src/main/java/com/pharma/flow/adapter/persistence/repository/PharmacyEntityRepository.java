package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.PharmacyEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PharmacyEntityRepository extends ReactiveCrudRepository<PharmacyEntity, UUID> {}
