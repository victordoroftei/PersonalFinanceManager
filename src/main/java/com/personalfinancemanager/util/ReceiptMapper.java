package com.personalfinancemanager.util;

import com.personalfinancemanager.domain.dto.ReceiptModel;
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

    public static ReceiptModel scannedDtoToModel(ReceiptScannedDto dto) {
        return ReceiptModel.builder()
                .calculatedTotal(dto.getCalculatedTotal())
                .detectedTotal(dto.getDetectedTotal())
                .retailer(dto.getRetailer())
                .imagePath(dto.getImagePath())
                .receiptDate(dto.getReceiptDate())
                .build();
    }

    public static ReceiptEntity modelToEntity(ReceiptModel model) {
        return ReceiptEntity.builder()
                .calculatedTotal(model.getCalculatedTotal())
                .detectedTotal(model.getDetectedTotal())
                .retailer(model.getRetailer())
                .imagePath(model.getImagePath())
                .receiptDate(model.getReceiptDate())
                .build();
    }
}
