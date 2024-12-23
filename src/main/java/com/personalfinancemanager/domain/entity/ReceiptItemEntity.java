package com.personalfinancemanager.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column(name = "itemPrice")
    private Double itemPrice;
}
