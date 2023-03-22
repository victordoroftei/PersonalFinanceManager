package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.PercentageModel;
import com.personalfinancemanager.repository.ExpenseRepository;
import com.personalfinancemanager.repository.InvoiceRepository;
import com.personalfinancemanager.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ReceiptRepository receiptRepository;

    private final InvoiceRepository invoiceRepository;

    private final ExpenseRepository expenseRepository;


    public PercentageModel getStatistics(Integer userId) {
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
