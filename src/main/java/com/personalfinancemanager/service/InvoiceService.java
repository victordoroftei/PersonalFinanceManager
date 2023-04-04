package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.model.InvoiceModel;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.repository.ExpenseRepository;
import com.personalfinancemanager.repository.InvoiceRepository;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.service.email.EmailService;
import com.personalfinancemanager.util.mapper.ExpenseMapper;
import com.personalfinancemanager.util.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    private final NotificationHistoryService notificationHistoryService;

    private final EmailService emailService;

    public void addInvoice(InvoiceModel model, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            InvoiceEntity invoiceEntity = InvoiceMapper.modelToEntity(model);

            invoiceEntity.setUser(userOptional.get());
            invoiceEntity.setInsertedDate(LocalDateTime.now());

            if (invoiceEntity.getPaid().equals(false)) {
                invoiceEntity.setPaidDate(null);
            } else {
                expenseRepository.save(ExpenseMapper.invoiceEntityToExpenseEntity(invoiceEntity));
            }

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

    public Set<Integer> getPossibleInvoiceYears(Integer userId) {
        List<InvoiceEntity> entities = invoiceRepository.findAllByUserId(userId);
        Set<Integer> possibleYears = new TreeSet<>();
        entities.forEach(x -> possibleYears.add(x.getInvoiceDate().getYear()));

        return possibleYears;
    }

    public List<InvoiceEntity> getInvoicesForMonthAndYear(Integer year, Integer month, Integer userId) {
        List<InvoiceEntity> entities = invoiceRepository.findAllByUserId(userId);

        List<InvoiceEntity> filteredEntities = entities.stream()
                .filter(x -> {
                    if (year == -1) {
                        return true;
                    }

                    if (month != 0 && month != -1) {
                        return x.getInvoiceDate().getYear() == year && x.getInvoiceDate().getMonthValue() == month;
                    }

                    return x.getInvoiceDate().getYear() == year;
                })
                .collect(Collectors.toList());

        return filteredEntities;
    }

    public void payInvoice(InvoiceEntity invoice) {
        Integer invoiceId = invoice.getId();
        Optional<InvoiceEntity> invoiceEntityOptional = invoiceRepository.findById(invoiceId);

        if (invoiceEntityOptional.isPresent()) {
            InvoiceEntity entity = invoiceEntityOptional.get();

            entity.setPaid(true);
            entity.setPaidDate(LocalDateTime.now());

            invoiceRepository.save(entity);
            expenseRepository.save(ExpenseMapper.invoiceEntityToExpenseEntity(entity));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no invoice with the provided ID!");
        }
    }

    public List<InvoiceEntity> getDueInvoices(Integer userId) {
        List<InvoiceEntity> entities = invoiceRepository.findAllByUserIdAndNotPaidOrdered(userId);

        LocalDateTime now = LocalDateTime.now();
        List<InvoiceEntity> filteredEntities = entities.stream()
                .filter(x -> {
                    return calculateDayDifferenceBetweenDates(x.getDueDate(), now) <= 7;
                })
                .collect(Collectors.toList());

        for (InvoiceEntity entity : filteredEntities) {
            entity.setUser(null);
        }

        return filteredEntities;
    }

    @Async
    @Scheduled(fixedRate = 86400000)    // 24 hour fixed rate
    public void sendDueInvoicesReminder() {
        System.out.println("--------------------- DUE INVOICES REMINDER ----------------------");
        if (!notificationHistoryService.checkIfRecordAlreadyExists()) {
            System.out.println("--------------------- SENDING DUE INVOICES REMINDER EMAILS ----------------------");

            Map<UserEntity, List<InvoiceEntity>> map = getDueInvoiceMailingLists();
            for (UserEntity user : map.keySet()) {
                emailService.sendEmailAsync(user, map.get(user));
            }

            notificationHistoryService.addRecord();
        } else {
            System.out.println("--------------------- NOTIFICATION HISTORY RECORD ALREADY EXISTS ----------------------");
        }
    }

    private Map<UserEntity, List<InvoiceEntity>> getDueInvoiceMailingLists() {
        List<UserEntity> userEntities = userRepository.findAll();
        Map<UserEntity, List<InvoiceEntity>> mailingMap = new HashMap<>();

        for (UserEntity user : userEntities) {
            Integer id = user.getId();
            List<InvoiceEntity> dueInvoices = getDueInvoices(id);

            if (!dueInvoices.isEmpty()) {
                mailingMap.put(user, dueInvoices);
            }
        }

        return mailingMap;
    }

    private Long calculateDayDifferenceBetweenDates(LocalDateTime dueDate, LocalDateTime currentDate) {
        return ChronoUnit.DAYS.between(currentDate, dueDate) + 1;
    }
}
