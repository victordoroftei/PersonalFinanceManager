package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.InvoiceModel;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.repository.InvoiceRepository;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.util.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final UserRepository userRepository;

    public void addInvoice(InvoiceModel model, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            InvoiceEntity invoiceEntity = InvoiceMapper.modelToEntity(model);

            invoiceEntity.setUser(userOptional.get());
            invoiceEntity.setInsertedDate(LocalDateTime.now());

            invoiceRepository.save(invoiceEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find user when saving new invoice!");
        }
    }

    public InvoiceModel getInvoiceById(String invoiceIdStr) {
        int invoiceId;
        try {
            invoiceId = Integer.parseInt(invoiceIdStr);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid invoice ID!");
        }

        Optional<InvoiceEntity> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isPresent()) {
            InvoiceEntity invoice = invoiceOptional.get();
            return InvoiceMapper.entityToModel(invoice);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no invoice with the provided ID!");
        }

    }
}
