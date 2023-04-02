package com.personalfinancemanager.domain.entity;

import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;
import lombok.ToString;

@ToString
public class ExpenseTypeSum {

    private ExpenseTypeEnum expenseType;

    private Double typeSum;

    public ExpenseTypeSum(ExpenseTypeEnum expenseType, Double typeSum) {
        this.expenseType = expenseType;
        this.typeSum = typeSum;
    }

    public ExpenseTypeEnum getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseTypeEnum expenseType) {
        this.expenseType = expenseType;
    }

    public Double getTypeSum() {
        return typeSum;
    }

    public void setTypeSum(Double typeSum) {
        this.typeSum = typeSum;
    }
}
