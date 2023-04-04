package com.personalfinancemanager.service.email;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private static final String SENDER = "recycleitapplication@gmail.com";

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void sendEmailAsync(UserEntity user, List<InvoiceEntity> invoices) {
        executorService.execute(() -> {
            try {
                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

                String template = loadTemplate("src/main/resources/templates/template.html");
                String invoicesString = "";
                for (InvoiceEntity invoice : invoices) {
                    invoicesString += getListItemForInvoice(invoice);
                }

                template = template.replace("{0}", user.getFirstname());
                template = template.replace("{1}", String.valueOf(invoices.size()));
                template = template.replace("{2}", invoicesString);

                helper.setFrom(SENDER);
                helper.setTo(user.getEmailAddress());
                helper.setText(template, true);
                helper.setSubject("Due Invoices Reminder");

                javaMailSender.send(mailMessage);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        });
    }

    private String getListItemForInvoice(InvoiceEntity invoice) {
        String[] splitArr = invoice.getAmount().toString().split("\\.");
        String invoiceAmountString = splitArr[0] + ".";
        if (splitArr[1].length() == 0) {
            invoiceAmountString += "00";
        } else if (splitArr[1].length() == 1) {
            invoiceAmountString += splitArr[1].charAt(0) + "0";
        } else {
            invoiceAmountString += splitArr[1].charAt(0);
            invoiceAmountString += splitArr[1].charAt(1);
        }

        int dayDiff = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), invoice.getDueDate());
        String listItem;
        if (dayDiff <= 0) {
            listItem = "<li style='color:red'>";
        } else {
            listItem = "<li>";
        }

        listItem += String.format("<strong>%s</strong> - %s RON - %s - <strong>Due",
                invoice.getRetailer(), invoiceAmountString, invoice.getType().name());

        dayDiff = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), invoice.getDueDate());
        if (dayDiff < 0) {
            listItem += String.format(" %d days ago", (-1) * dayDiff);
        } else if (ChronoUnit.DAYS.between(LocalDateTime.now(), invoice.getDueDate()) == 0) {
            listItem += " today";
        } else {
            listItem += String.format(" in %d days", dayDiff);
        }

        listItem += "</strong></li><br>";
        return listItem;
    }

    private String loadTemplate(String path) {
        String content;
        try {
            content = new Scanner(new File(path)).useDelimiter("\\Z").next();
            System.out.println(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
