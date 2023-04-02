package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.dto.ExpenseModel;
import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;

public class ExpenseMapper {

    public static ExpenseEntity modelToEntity(ExpenseModel model) {
        return ExpenseEntity.builder()
                .price(model.getPrice())
                .description(model.getDescription())
                .type(model.getType())
                .expenseDate(model.getExpenseDate())
                .build();
    }

    public static ExpenseModel entityToModel(ExpenseEntity entity) {
        return ExpenseModel.builder()
                .price(entity.getPrice())
                .description(entity.getDescription())
                .type(entity.getType())
                .expenseDate(entity.getExpenseDate())
                .build();
    }

    public static ExpenseEntity receiptEntityToExpenseEntity(ReceiptEntity entity) {
        return ExpenseEntity.builder()
                .price(entity.getCalculatedTotal())
                .description("Receipt")
                .type(ExpenseTypeEnum.RECEIPT)
                .expenseDate(entity.getReceiptDate())
                .build();
    }

    public static ExpenseEntity invoiceEntityToExpenseEntity(InvoiceEntity entity) {
        return ExpenseEntity.builder()
                .price(entity.getAmount())
                .description("Invoice")
                .type(ExpenseTypeEnum.INVOICE)
                .expenseDate(entity.getPaidDate())
                .build();
    }
}
