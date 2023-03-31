package com.personalfinancemanager.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;
import com.personalfinancemanager.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseModel {

    private Double price;

    private ExpenseTypeEnum type;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime insertedDate;
}
