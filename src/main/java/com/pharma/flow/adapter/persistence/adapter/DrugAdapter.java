package com.pharma.flow.adapter.persistence.adapter;

import com.pharma.flow.adapter.persistence.entity.DrugEntity;
import com.pharma.flow.adapter.persistence.mapper.DrugMapper;
import com.pharma.flow.adapter.persistence.repository.DrugEntityRepository;
import com.pharma.flow.domain.model.Drug;
import com.pharma.flow.domain.repository.DrugRepository;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrugAdapter implements DrugRepository {

    private final DrugEntityRepository repository;
    private final DrugMapper mapper;

    @Override
    public Mono<Drug> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Flux<Drug> findAllValidByName(String name) {
        return repository.findByNameAndExpiryDateAfter(name, LocalDate.now()).map(mapper::toDomain);
    }

    @Override
    public Mono<Drug> save(Drug drug) {
        DrugEntity entity = mapper.toEntity(drug);
        log.debug("Saving drug:{} drugEntity: {}", drug, entity);
        return repository.save(entity).map(mapper::toDomain);
    }
}
