package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;
import com.personalfinancemanager.domain.model.ExpensesMonthlyStatisticsModel;
import com.personalfinancemanager.domain.model.PercentageModel;
import com.personalfinancemanager.domain.model.YearlyStatisticsModel;
import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.repository.ExpenseRepository;
import com.personalfinancemanager.repository.InvoiceRepository;
import com.personalfinancemanager.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                        return  !ExpenseTypeEnum.RECEIPT.equals(x.getType())
                                && !ExpenseTypeEnum.INVOICE.equals(x.getType())
                                && x.getExpenseDate().getYear() == year
                                && x.getExpenseDate().getMonthValue() == finalMonth;
                    } else {
                        return  !ExpenseTypeEnum.RECEIPT.equals(x.getType())
                                && !ExpenseTypeEnum.INVOICE.equals(x.getType())
                                &&x.getExpenseDate().getYear() == year;
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

    public YearlyStatisticsModel getYearlyStatisticsByMonth(Integer year, Integer userId) {
        List<ReceiptEntity> receiptEntities = receiptRepository.findAllByUserId(userId);
        List<Double> receiptSums = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int finalI = i;
            double sum = receiptEntities.stream()
                    .filter(x -> x.getReceiptDate().getYear() == year && x.getReceiptDate().getMonthValue() == finalI)
                    .mapToDouble(ReceiptEntity::getCalculatedTotal)
                    .sum();

            receiptSums.add(sum);
        }

        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByUserId(userId);
        List<Double> invoiceSums = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int finalI = i;
            double sum = invoiceEntities.stream()
                    .filter(x -> x.getPaid() && x.getPaidDate().getYear() == year && x.getPaidDate().getMonthValue() == finalI)
                    .mapToDouble(InvoiceEntity::getAmount)
                    .sum();

            invoiceSums.add(sum);
        }

        List<ExpenseEntity> expenseEntities = expenseRepository.findAllByUserId(userId);
        List<Double> expenseSums = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int finalI = i;
            double sum = expenseEntities.stream()
                    .filter(x -> !ExpenseTypeEnum.RECEIPT.equals(x.getType())
                            && !ExpenseTypeEnum.INVOICE.equals(x.getType())
                            && x.getExpenseDate().getYear() == year
                            && x.getExpenseDate().getMonthValue() == finalI)
                    .mapToDouble(ExpenseEntity::getPrice)
                    .sum();

            expenseSums.add(sum);
        }

        return YearlyStatisticsModel.builder()
                .year(year)
                .receipts(receiptSums)
                .invoices(invoiceSums)
                .expenses(expenseSums)
                .build();
    }

    public ExpensesMonthlyStatisticsModel getExpensesGroupedByType(Integer year, Integer month, Integer userId) {
        List<ExpenseEntity> entities = expenseRepository.findAllByUserId(userId);
        List<ExpenseEntity> filteredEntities = entities.stream()
                .filter(x -> {
                    if (month <= 0) {
                        return x.getExpenseDate().getYear() == year;
                    }

                    return x.getExpenseDate().getYear() == year && x.getExpenseDate().getMonthValue() == month;
                })
                .collect(Collectors.toList());

        List<String> possibleTypes = List.of("RECEIPT", "INVOICE", "RENT", "FUEL", "FOOD", "TRANSPORT", "EDUCATION", "CLOTHING", "OTHER");
        Map<String, List<Double>> map = new HashMap<>();
        if (month <= 0) {
            for (String type : possibleTypes) {
                List<Double> list = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    int finalI = i;
                    double sum = filteredEntities.stream()
                            .filter(x -> type.equals(x.getType().name()) && x.getExpenseDate().getMonthValue() == finalI)
                            .mapToDouble(ExpenseEntity::getPrice)
                            .sum();

                    list.add(sum);
                }
                map.put(type, list);
            }
        } else {
            for (String type : possibleTypes) {
                List<Double> list = new ArrayList<>();
                for (int i = 1; i <= 31; i++) {
                    int finalI = i;
                    double sum = filteredEntities.stream()
                            .filter(x -> type.equals(x.getType().name()) && x.getExpenseDate().getDayOfMonth() == finalI)
                            .mapToDouble(ExpenseEntity::getPrice)
                            .sum();

                    list.add(sum);
                }
                map.put(type, list);
            }
        }

        ExpensesMonthlyStatisticsModel model = ExpensesMonthlyStatisticsModel.builder()
                .year(year)
                .month(month)
                .receipt(map.get("RECEIPT"))
                .invoice(map.get("INVOICE"))
                .rent(map.get("RENT"))
                .food(map.get("FOOD"))
                .fuel(map.get("FUEL"))
                .transport(map.get("TRANSPORT"))
                .education(map.get("EDUCATION"))
                .clothing(map.get("CLOTHING"))
                .other(map.get("OTHER"))
                .build();

        return model;
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
