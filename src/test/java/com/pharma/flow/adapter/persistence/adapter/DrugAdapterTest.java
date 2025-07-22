package com.pharma.flow.adapter.persistence.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.pharma.flow.adapter.persistence.entity.DrugEntity;
import com.pharma.flow.adapter.persistence.mapper.DrugMapper;
import com.pharma.flow.adapter.persistence.repository.DrugEntityRepository;
import com.pharma.flow.domain.model.Drug;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DrugAdapterTest {

    private final DrugEntityRepository repository = Mockito.mock(DrugEntityRepository.class);
    private final DrugMapper mapper = Mockito.mock(DrugMapper.class);
    private final DrugAdapter adapter = new DrugAdapter(repository, mapper);

    @Test
    void shouldFindDrugById() {
        // given
        UUID id = UUID.randomUUID();
        Drug drug = Drug.builder()
                .id(id)
                .name("Aspirin")
                .manufacturer("Pharma Inc.")
                .batchNumber("B123")
                .expiryDate(LocalDate.now().plusDays(30))
                .stock(100)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        DrugEntity entity = new DrugEntity();
        Mockito.when(repository.findById(eq(id))).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomain(any(DrugEntity.class))).thenReturn(drug);

        // when
        Mono<Drug> result = adapter.findById(id);

        // then
        StepVerifier.create(result).expectNext(drug).verifyComplete();
        Mockito.verify(repository).findById(eq(id));
        Mockito.verify(mapper).toDomain(any(DrugEntity.class));
    }

    @Test
    void shouldFindAllValidDrugsByName() {
        // given
        String name = "Aspirin";
        Drug drug = Drug.builder()
                .id(UUID.randomUUID())
                .name(name)
                .manufacturer("Pharma Inc.")
                .batchNumber("B123")
                .expiryDate(LocalDate.now().plusDays(30))
                .stock(100)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        DrugEntity entity = new DrugEntity();
        Mockito.when(repository.findByNameAndExpiryDateAfter(eq(name), any(LocalDate.class)))
                .thenReturn(Flux.just(entity));
        Mockito.when(mapper.toDomain(any(DrugEntity.class))).thenReturn(drug);

        // when
        Flux<Drug> result = adapter.findAllValidByName(name);

        // then
        StepVerifier.create(result).expectNext(drug).verifyComplete();
        Mockito.verify(repository).findByNameAndExpiryDateAfter(eq(name), any(LocalDate.class));
        Mockito.verify(mapper).toDomain(any(DrugEntity.class));
    }

    @Test
    void shouldSaveDrug() {
        // given
        Drug drug = Drug.builder()
                .id(UUID.randomUUID())
                .name("Aspirin")
                .manufacturer("Pharma Inc.")
                .batchNumber("B123")
                .expiryDate(LocalDate.now().plusDays(30))
                .stock(100)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        DrugEntity entity = new DrugEntity();
        Mockito.when(mapper.toEntity(any(Drug.class))).thenReturn(entity);
        Mockito.when(repository.save(any(DrugEntity.class))).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomain(any(DrugEntity.class))).thenReturn(drug);

        // when
        Mono<Drug> result = adapter.save(drug);

        // then
        StepVerifier.create(result).expectNext(drug).verifyComplete();
        Mockito.verify(repository).save(any(DrugEntity.class));
        Mockito.verify(mapper).toEntity(eq(drug));
        Mockito.verify(mapper).toDomain(any(DrugEntity.class));
    }
}
