Pharma Supply Chain and Prescription Fulfillment System。
background：You are tasked with building a Pharma Supply Chain and Prescription
Fulfillment System for a pharmaceutical company. The system will manage the
inventory of drugs, prescriptions, and pharmacies, as well as the logistics of
fulfilling patient prescriptions.

项目使用了spring-boot，spring-boot-webflux，spring-boot-starter-data-r2dbc，r2dbc-postgresql，flyway-database-postgresql，org.mapstruct:mapstruct，lombok。

项目采用了DDD+clean architect的架构，如下
```
-adapter
    -persistence
        -adapter
        -entity
        -mapper
        -repository
    -web
        -adapter
        -advice
        -controller
        -dto
        -exception
        -mapper
        -security
-application
    -service
-domain
    -model
    -repository
```
数据库当前的设计已经更新为如下：
https://dbdiagram.io/d/rx-6879f79bf413ba350881eae5

domain层对外的repository也实现完毕，具体如下
```
public interface DrugRepository {
    Mono<Drug> save(Drug drug);
    Mono<Drug> findById(UUID id);
    Flux<Drug> findAllValidByName(String name);
}

public interface PharmacyRepository {
    Mono<Pharmacy> findById(UUID pharmacyId);
    Flux<Pharmacy> findAll();
    Mono<Pharmacy> savePharmacy(Pharmacy pharmacy);
    Mono<PharmacyDrugAllocation> saveAllocation(PharmacyDrugAllocation allocation);
}

public interface PrescriptionRepository {
    Mono<Prescription> findById(UUID id);
    Mono<Void> updateStatus(UUID id, PrescriptionStatus status, Instant fulfilledAt);
    Mono<Prescription> savePrescription(Prescription prescription);
}
```

需求：
Drugs are only dispensed if they are within their expiry date.
Pharmacies can only dispense specific drugs they are contracted for.
Pharmacies must not dispense more than their allocated amount of a specific drug.
Prescriptions include multiple drugs, with a specific dosage for each drug.
A prescription is valid only if all requested drugs are available and within the pharmacy's allocation limits.
If any requested drug is unavailable, the prescription must fail with an appropriate error message.
When a prescription is fulfilled, the stock for the dispensed drugs must be updated.
If stock is insufficient for any drug in the prescription, the entire prescription must fail.

Api需求
Add a Drug: Add a drug to the inventory, specifying its details and stock. 需要注意的是，
1. manufacturer 与 batch number 相同的时候，expiry date是相同的
2. 多次添加 drug 时，存在相同的 manufacturer 和 batch number

List Pharmacies: Retrieve all pharmacies and their contracted drugs.
1. 我们可以创建，修改，关闭Pharmacy
2. 我们可以为Pharmacy添加pharmacy_drug_allocation，修改pharmacy_drug_allocation的allocation_limit，但所有Pharmacy的allocation_limit总和，不应该超过drug中没有到expiry date的数量总和

Create a Prescription: Submit a prescription for a patient at a specific pharmacy.

Fulfill a Prescription: Process the prescription and update stock levels accordingly.

Get Audit Logs: Retrieve audit logs with filters for patient, pharmacy, or success/failure. 采用data.annotation搭配JPA和treadlocal，切面编程来实现。
