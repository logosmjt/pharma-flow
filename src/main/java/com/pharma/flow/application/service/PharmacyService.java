package com.pharma.flow.application.service;

import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.domain.model.Pharmacy;
import com.pharma.flow.domain.model.PharmacyDrugAllocation;
import com.pharma.flow.domain.repository.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final TransactionalOperator tx;

    public Mono<Pharmacy> findById(UUID pharmacyId) {
        return pharmacyRepository
                .findById(pharmacyId)
                .switchIfEmpty(Mono.error(new NotFoundException("Pharmacy not found")));
    }

    public Flux<Pharmacy> listPharmacies() {
        return pharmacyRepository.findAll();
    }

    public Mono<Pharmacy> createPharmacy(Pharmacy pharmacy) {
        return pharmacyRepository.savePharmacy(pharmacy);
    }

    public Mono<PharmacyDrugAllocation> createAllocation(PharmacyDrugAllocation allocation) {
        return tx.transactional(pharmacyRepository.saveAllocation(allocation));
    }

    public Mono<PharmacyDrugAllocation> updateAllocationLimit(UUID allocationId, int newLimit) {
        return pharmacyRepository
                .findAllocationById(allocationId)
                .switchIfEmpty(Mono.error(new NotFoundException("Allocation not found")))
                .flatMap(allocation -> {
                    allocation.setAllocationLimit(newLimit);
                    return pharmacyRepository.saveAllocation(allocation);
                });
    }
}
