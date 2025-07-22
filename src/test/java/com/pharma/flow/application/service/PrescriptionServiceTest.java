package com.pharma.flow.application.service;

import static org.mockito.ArgumentMatchers.any;

import com.pharma.flow.adapter.persistence.mapper.PrescriptionMapper;
import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.domain.model.*;
import com.pharma.flow.domain.repository.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @Mock
    private DrugRepository drugRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private TransactionalOperator tx;

    @Mock
    private PrescriptionMapper mapper;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private final UUID prescriptionId = UUID.randomUUID();
    private final UUID drugId = UUID.randomUUID();
    private final UUID pharmacyId = UUID.randomUUID();

    private Prescription mockPrescription;
    private Drug mockDrug;
    private PharmacyDrugAllocation mockAllocation;
    private AuditLog mockAuditLog;

    @BeforeEach
    void setup() {
        mockDrug = Drug.builder()
                .id(drugId)
                .expiryDate(LocalDate.now().plusDays(30))
                .stock(100)
                .build();

        mockAllocation = PharmacyDrugAllocation.builder()
                .drugId(drugId)
                .pharmacyId(pharmacyId)
                .allocationLimit(100)
                .build();

        mockPrescription = Prescription.builder()
                .id(prescriptionId)
                .pharmacyId(pharmacyId)
                .drugs(List.of())
                .build();

        mockAuditLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .build();

        Mockito.lenient()
                .when(tx.transactional(Mockito.any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldReturnPrescriptionById() {
        Mockito.when(prescriptionRepository.findById(prescriptionId)).thenReturn(Mono.just(mockPrescription));

        StepVerifier.create(prescriptionService.getPrescriptionById(prescriptionId))
                .expectNext(mockPrescription)
                .verifyComplete();
    }

    @Test
    void shouldErrorIfPrescriptionNotFound() {
        Mockito.when(prescriptionRepository.findById(prescriptionId)).thenReturn(Mono.empty());

        StepVerifier.create(prescriptionService.getPrescriptionById(prescriptionId))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void shouldSavePrescriptionSuccessfully() {
        Mockito.when(prescriptionRepository.savePrescription(Mockito.any())).thenReturn(Mono.just(mockPrescription));

        StepVerifier.create(prescriptionService.savePrescription(mockPrescription))
                .expectNext(mockPrescription)
                .verifyComplete();
    }

    @Test
    void shouldFailFulfillmentWithExpiredDrug() {
        mockDrug.setExpiryDate(LocalDate.now().minusDays(1));
        Mockito.when(prescriptionRepository.findById(prescriptionId)).thenReturn(Mono.just(mockPrescription));
        Mockito.when(drugRepository.findById(drugId)).thenReturn(Mono.just(mockDrug));
        Mockito.when(auditLogRepository.save(any())).thenReturn(Mono.just(mockAuditLog));
        Mockito.when(prescriptionRepository.updateStatus(
                        Mockito.any(), Mockito.eq(PrescriptionStatus.FAILED), Mockito.any()))
                .thenReturn(Mono.just(mockPrescription));

        StepVerifier.create(prescriptionService.fulfillment(prescriptionId))
                .expectNext(mockPrescription)
                .verifyComplete();
    }

    @Test
    void shouldFulfillPrescriptionSuccessfully() {
        Mockito.when(prescriptionRepository.findById(prescriptionId)).thenReturn(Mono.just(mockPrescription));
        Mockito.when(auditLogRepository.save(any())).thenReturn(Mono.just(mockAuditLog));
        Mockito.when(prescriptionRepository.updateStatus(
                        Mockito.any(), Mockito.eq(PrescriptionStatus.FULFILLED), Mockito.any()))
                .thenReturn(Mono.just(mockPrescription));
        Mockito.when(mapper.buildAuditLog(Mockito.any(), Mockito.eq(true), Mockito.isNull()))
                .thenReturn(AuditLog.builder().build());

        StepVerifier.create(prescriptionService.fulfillment(prescriptionId))
                .expectNext(mockPrescription)
                .verifyComplete();
    }
}
