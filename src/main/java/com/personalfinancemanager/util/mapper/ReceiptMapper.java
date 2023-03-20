package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.dto.ReceiptModel;
import com.personalfinancemanager.domain.dto.ReceiptScannedModel;
import com.personalfinancemanager.domain.entity.ReceiptEntity;

public class ReceiptMapper {

    public static ReceiptEntity scannedModelToEntity(ReceiptScannedModel scannedModel) {
        return ReceiptEntity.builder()
                .calculatedTotal(scannedModel.getCalculatedTotal())
                .detectedTotal(scannedModel.getDetectedTotal())
                .retailer(scannedModel.getRetailer())
                .imagePath(scannedModel.getImagePath())
                .receiptDate(scannedModel.getReceiptDate())
                .build();
    }

    public static ReceiptModel scannedModelToModel(ReceiptScannedModel scannedModel) {
        return ReceiptModel.builder()
                .calculatedTotal(scannedModel.getCalculatedTotal())
                .detectedTotal(scannedModel.getDetectedTotal())
                .retailer(scannedModel.getRetailer())
                .imagePath(scannedModel.getImagePath())
                .receiptDate(scannedModel.getReceiptDate())
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

    public static ReceiptModel entityToModel(ReceiptEntity entity) {
        return ReceiptModel.builder()
                .calculatedTotal(entity.getCalculatedTotal())
                .detectedTotal(entity.getDetectedTotal())
                .retailer(entity.getRetailer())
                .imagePath(entity.getImagePath())
                .receiptDate(entity.getReceiptDate())
                .build();
    }
}
