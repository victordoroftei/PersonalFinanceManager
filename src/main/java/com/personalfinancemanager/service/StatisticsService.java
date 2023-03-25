package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.PercentageModel;
import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.repository.ExpenseRepository;
import com.personalfinancemanager.repository.InvoiceRepository;
import com.personalfinancemanager.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ReceiptRepository receiptRepository;

    private final InvoiceRepository invoiceRepository;

    private final ExpenseRepository expenseRepository;


    public PercentageModel getStatistics(Integer year, Integer month, Integer userId) {
        if (year == -1 || year == 0) {
            Double receiptSum = receiptRepository.getSumByUserId(userId);
            Double invoiceSum = invoiceRepository.getSumByUserId(userId);
            Double expenseSum = expenseRepository.getSumByUserId(userId);

            if (receiptSum == null) {
                receiptSum = 0D;
            }

            if (invoiceSum == null) {
                invoiceSum = 0D;
            }

            if (expenseSum == null) {
                expenseSum = 0D;
            }

            //return buildPercentageModel(receiptSum, invoiceSum, expenseSum);
            return PercentageModel.builder()
                    .receipts(receiptSum)
                    .invoices(invoiceSum)
                    .expenses(expenseSum)
                    .build();
        }

        if (month == -1) {
            month = 0;
        }

        List<ReceiptEntity> receiptEntityList = receiptRepository.findAllByUserId(userId);
        int finalMonth = month;
        List<ReceiptEntity> filteredReceiptEntityList = receiptEntityList.stream()
                .filter(x -> {
                    if (finalMonth != 0) {
                        return x.getReceiptDate().getYear() == year && x.getReceiptDate().getMonthValue() == finalMonth;
                    } else {
                        return x.getReceiptDate().getYear() == year;
                    }
                })
                .collect(Collectors.toList());

        Double receiptSum = filteredReceiptEntityList.stream().mapToDouble(ReceiptEntity::getCalculatedTotal).sum();

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAllByUserId(userId);
        List<InvoiceEntity> filteredInvoiceEntityList = invoiceEntityList.stream()
                .filter(x -> {
                    if (finalMonth != 0) {
                        return x.getPaid() && x.getPaidDate().getYear() == year && x.getPaidDate().getMonthValue() == finalMonth;
                    } else {
                        return x.getPaid() && x.getPaidDate().getYear() == year;
                    }
                })
                .collect(Collectors.toList());

        Double invoiceSum = filteredInvoiceEntityList.stream().mapToDouble(InvoiceEntity::getAmount).sum();

        List<ExpenseEntity> expenseEntityList = expenseRepository.findAllByUserId(userId);
        List<ExpenseEntity> filteredExpenseEntityList = expenseEntityList.stream()
                .filter(x -> {
                    if (finalMonth != 0) {
                        return x.getInsertedDate().getYear() == year && x.getInsertedDate().getMonthValue() == finalMonth;
                    } else {
                        return x.getInsertedDate().getYear() == year;
                    }
                })
                .collect(Collectors.toList());

        Double expenseSum = filteredExpenseEntityList.stream().mapToDouble(ExpenseEntity::getPrice).sum();

        //return buildPercentageModel(receiptSum, invoiceSum, expenseSum);
        return PercentageModel.builder()
                .receipts(receiptSum)
                .invoices(invoiceSum)
                .expenses(expenseSum)
                .build();
    }

    private PercentageModel buildPercentageModel(Double receiptSum, Double invoiceSum, Double expenseSum) {
        Double totalSum = receiptSum + invoiceSum + expenseSum;

        Double receiptPercentage = (receiptSum / totalSum) * 100;
        Double invoicePercentage = (invoiceSum / totalSum) * 100;
        Double expensePercentage = (expenseSum / totalSum) * 100;

        return PercentageModel.builder()
                .expenses(expensePercentage)
                .invoices(invoicePercentage)
                .receipts(receiptPercentage)
                .build();
    }
}
