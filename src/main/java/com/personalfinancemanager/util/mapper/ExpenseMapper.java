package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.dto.ExpenseModel;
import com.personalfinancemanager.domain.dto.InvoiceModel;
import com.personalfinancemanager.domain.dto.ReceiptModel;
import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;

public class ExpenseMapper {

    public static ExpenseEntity modelToEntity(ExpenseModel model) {
        return ExpenseEntity.builder()
                .price(model.getPrice())
                .type(model.getType())
                .insertedDate(model.getInsertedDate())
                .build();
    }

    public static ExpenseModel entityToModel(ExpenseEntity entity) {
        return ExpenseModel.builder()
                .price(entity.getPrice())
                .type(entity.getType())
                .insertedDate(entity.getInsertedDate())
                .build();
    }

    public static ExpenseEntity receiptEntityToExpenseEntity(ReceiptEntity entity) {
        return ExpenseEntity.builder()
                .price(entity.getCalculatedTotal())
                .type(ExpenseTypeEnum.RECEIPT)
                .insertedDate(entity.getReceiptDate())
                .build();
    }

    public static ExpenseEntity invoiceEntityToExpenseEntity(InvoiceEntity entity) {
        return ExpenseEntity.builder()
                .price(entity.getAmount())
                .type(ExpenseTypeEnum.INVOICE)
                .insertedDate(entity.getPaidDate())
                .build();
    }
}
