package com.pharma.flow.adapter.persistence.repository;

import com.pharma.flow.adapter.persistence.entity.DrugEntity;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DrugEntityRepository extends ReactiveCrudRepository<DrugEntity, UUID> {
    @Query("""
        SELECT * FROM drug
        WHERE name = :name
        AND expiry_date > CURRENT_DATE
    """)
    Flux<DrugEntity> findAllValidByName(@Param("name") String name);

    Flux<DrugEntity> findByNameAndExpiryDateAfter(String name, LocalDate expiryDate);
}
