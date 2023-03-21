package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.dto.InvoiceModel;
import com.personalfinancemanager.domain.entity.InvoiceEntity;

public class InvoiceMapper {

    public static InvoiceEntity modelToEntity(InvoiceModel model) {
        return InvoiceEntity.builder()
                .retailer(model.getRetailer())
                .amount(model.getAmount())
                .type(model.getType())
                .invoiceDate(model.getInvoiceDate())
                .dueDate(model.getDueDate())
                .insertedDate(model.getInsertedDate())
                .build();
    }

    public static InvoiceModel entityToModel(InvoiceEntity entity) {
        return InvoiceModel.builder()
                .retailer(entity.getRetailer())
                .amount(entity.getAmount())
                .type(entity.getType())
                .invoiceDate(entity.getInvoiceDate())
                .dueDate(entity.getDueDate())
                .insertedDate(entity.getInsertedDate())
                .build();
    }
}
