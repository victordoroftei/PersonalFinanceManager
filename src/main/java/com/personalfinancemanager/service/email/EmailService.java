package com.personalfinancemanager.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private static final String SENDER = "recycleitapplication@gmail.com";

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void sendEmailAsync() {
        executorService.execute(() -> {
            try {
                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

                String template = loadTemplate("src/main/resources/templates/template.html");

                helper.setFrom(SENDER);
                helper.setTo("victordoro78@yahoo.com");
                helper.setText(template, true);
                helper.setSubject("Async email test");

                javaMailSender.send(mailMessage);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        });
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
