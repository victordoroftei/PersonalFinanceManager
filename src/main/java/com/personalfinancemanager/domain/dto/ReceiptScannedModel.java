package com.personalfinancemanager.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ReceiptScannedModel {

    private String items;

    private Double calculatedTotal;

    private Double detectedTotal;

    private String retailer;

    private String imagePath;

    private String filePath;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime receiptDate;
}
