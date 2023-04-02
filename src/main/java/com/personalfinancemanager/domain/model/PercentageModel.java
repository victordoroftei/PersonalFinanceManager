package com.personalfinancemanager.domain.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PercentageModel {

    private Double receipts;

    private Double invoices;

    private Double expenses;

}
