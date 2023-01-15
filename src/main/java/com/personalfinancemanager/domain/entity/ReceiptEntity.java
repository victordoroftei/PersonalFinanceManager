package com.personalfinancemanager.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "receipts")
public class ReceiptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "calculated_total")
    private Double calculatedTotal;

    @Column(name = "detected_total")
    private Double detectedTotal;

    @Column(name = "retailer")
    private String retailer;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "receipt_date")
    private LocalDateTime receiptDate;

    @Column(name = "inserted_date")
    private LocalDateTime insertedDate;
}
