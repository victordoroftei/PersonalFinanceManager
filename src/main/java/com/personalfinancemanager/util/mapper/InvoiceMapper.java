package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.model.InvoiceModel;
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
                .paidDate(model.getPaidDate())
                .paid(model.getPaid())
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
                .paidDate(entity.getPaidDate())
                .paid(entity.getPaid())
                .build();
    }
}
