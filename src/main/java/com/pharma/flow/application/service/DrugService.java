package com.pharma.flow.application.service;

import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.domain.model.Drug;
import com.pharma.flow.domain.repository.DrugRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrugService {
    private final DrugRepository drugRepository;

    public Mono<Drug> createDrug(Drug drug) {
        return drugRepository.save(drug);
    }

    public Mono<Drug> updateDrugStock(UUID drugId, int newStock) {
        return drugRepository
                .findById(drugId)
                .switchIfEmpty(Mono.error(new NotFoundException("Drug not found")))
                .flatMap(drug -> {
                    drug.setStock(newStock);
                    return drugRepository.save(drug);
                });
    }

    public Mono<Drug> findById(UUID id) {
        return drugRepository.findById(id);
    }

    public Flux<Drug> findValidDrugsByName(String name) {
        return drugRepository.findAllValidByName(name);
    }
}
