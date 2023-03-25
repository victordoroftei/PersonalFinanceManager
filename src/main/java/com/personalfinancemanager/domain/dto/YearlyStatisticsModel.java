package com.personalfinancemanager.domain.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class YearlyStatisticsModel {

    private Integer year;

    private List<Double> receipts;

    private List<Double> invoices;

    private List<Double> expenses;
}
