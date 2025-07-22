package com.pharma.flow.domain.repository;

import com.pharma.flow.domain.model.Drug;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DrugRepository {
    Mono<Drug> save(Drug drug);

    Mono<Drug> findById(UUID id);

    Flux<Drug> findAllValidByName(String name);
}
