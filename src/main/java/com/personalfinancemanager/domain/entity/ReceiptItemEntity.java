package com.personalfinancemanager.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "receipt_items")
public class ReceiptItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "receipt_id", referencedColumnName = "id")
    private ReceiptEntity receipt;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private Double price;
}
