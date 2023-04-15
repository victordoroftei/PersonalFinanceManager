package com.personalfinancemanager.service.sms;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.service.SmsHistoryService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    @Value("${spring.sms.account-sid}")
    private String accountSid;

    @Value("${spring.sms.auth-token}")
    private String authToken;

    @Value("${spring.sms.number}")
    private String number;

    private final SmsHistoryService smsHistoryService;

    public void sendSms(UserEntity user, Integer smsDays, List<InvoiceEntity> invoices) {
        try {
            Twilio.init(accountSid, authToken);

            PhoneNumber toNumber = new PhoneNumber(user.getPhoneNumber());
            PhoneNumber fromNumber = new PhoneNumber(number);
            String body = buildSmsBody(user, smsDays, invoices);

            Message message = Message
                    .creator(toNumber, fromNumber, body)
                    .create();

            System.out.println(message.getSid());

            smsHistoryService.addSmsHistoryRecord(user, body);
        } catch (Exception e) {
            log.error("Exception occurred when sending message to phone_number={}", user.getPhoneNumber(), e);
            e.printStackTrace();
        }
    }

    public String buildSmsBody(UserEntity user, Integer smsDays, List<InvoiceEntity> invoices) {
        String body = String.format("Hello %s!\nYou have %d invoice(s) due in %d days:\n\n",
                user.getFirstname(), invoices.size(), smsDays);

        for (InvoiceEntity invoice : invoices) {
            body += String.format("%s - %.2f RON - %s\n",
                    invoice.getRetailer(), invoice.getAmount(), invoice.getType().name());
        }

        return body;
    }
}
