package com.personalfinancemanager.domain.entity;

import com.personalfinancemanager.domain.enums.InvoiceTypeEnum;
import com.personalfinancemanager.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "invoices")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "retailer")
    private String retailer;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InvoiceTypeEnum type;

    @Column(name = "invoice_date")
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime invoiceDate;

    @Column(name = "due_date")
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dueDate;

    @Column(name = "inserted_date")
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime insertedDate;
}
