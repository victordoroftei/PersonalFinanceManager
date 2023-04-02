package com.personalfinancemanager.domain.model;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpensesMonthlyStatisticsModel {

    private Integer year;

    private Integer month;

    private List<Double> receipt;

    private List<Double> invoice;

    private List<Double> rent;

    private List<Double> fuel;

    private List<Double> food;

    private List<Double> transport;

    private List<Double> education;

    private List<Double> clothing;

    private List<Double> other;
}
