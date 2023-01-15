package com.personalfinancemanager.domain.entity;

import com.personalfinancemanager.domain.enums.ExpenseTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "price")
    private Double price;

    @Column(name = "type")
    private ExpenseTypeEnum type;

    @Column(name = "inserted_date")
    private LocalDateTime insertedDate;
}
