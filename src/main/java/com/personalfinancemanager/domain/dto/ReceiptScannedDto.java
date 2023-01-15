package com.personalfinancemanager.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReceiptScannedDto {

    private String items;

    private Double calculatedTotal;

    private Double detectedTotal;

    private String retailer;

    private String filePath;

    private LocalDateTime receiptDate;
}
