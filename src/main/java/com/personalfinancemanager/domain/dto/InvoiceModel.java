package com.personalfinancemanager.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personalfinancemanager.domain.enums.InvoiceTypeEnum;
import com.personalfinancemanager.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceModel {

    private String retailer;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private InvoiceTypeEnum type;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime invoiceDate;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dueDate;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime insertedDate;
}
