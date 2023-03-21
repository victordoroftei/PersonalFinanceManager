package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

}
