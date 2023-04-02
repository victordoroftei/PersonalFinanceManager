package com.personalfinancemanager.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "notification_history")
public class NotificationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "notification_date")
    private Date notificationDate;
}
