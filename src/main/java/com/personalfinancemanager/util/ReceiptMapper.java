package com.personalfinancemanager.util;

import com.personalfinancemanager.domain.dto.ReceiptExtractedData;
import com.personalfinancemanager.domain.dto.ReceiptScannedDto;
import com.personalfinancemanager.domain.entity.ReceiptEntity;

public class ReceiptMapper {

    public static ReceiptEntity scannedDtoToEntity(ReceiptScannedDto dto) {
        return ReceiptEntity.builder()
                .calculatedTotal(dto.getCalculatedTotal())
                .detectedTotal(dto.getDetectedTotal())
                .retailer(dto.getRetailer())
                .imagePath(dto.getImagePath())
                .receiptDate(dto.getReceiptDate())
                .build();
    }

    public static ReceiptExtractedData scannedDtoToExtractedData(ReceiptScannedDto dto) {
        return ReceiptExtractedData.builder()
                .calculatedTotal(dto.getCalculatedTotal())
                .detectedTotal(dto.getDetectedTotal())
                .retailer(dto.getRetailer())
                .imagePath(dto.getImagePath())
                .receiptDate(dto.getReceiptDate())
                .build();
    }
}
