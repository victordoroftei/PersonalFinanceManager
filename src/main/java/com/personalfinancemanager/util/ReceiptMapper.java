package com.personalfinancemanager.util;

import com.personalfinancemanager.domain.dto.ReceiptScannedDto;
import com.personalfinancemanager.domain.entity.ReceiptEntity;

public class ReceiptMapper {

    public static ReceiptEntity scannedDtoToEntity(ReceiptScannedDto dto) {
        return ReceiptEntity.builder()
                .calculatedTotal(dto.getCalculatedTotal())
                .detectedTotal(dto.getDetectedTotal())
                .retailer(dto.getRetailer())
                .filePath(dto.getFilePath())
                .receiptDate(dto.getReceiptDate())
                .build();
    }
}
