package com.pharma.flow.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Drug {
    private UUID id;
    private String name;
    private String manufacturer;
    private String batchNumber;
    private LocalDate expiryDate;
    private int stock;
    private Instant createdAt;
    private Instant updatedAt;
}
