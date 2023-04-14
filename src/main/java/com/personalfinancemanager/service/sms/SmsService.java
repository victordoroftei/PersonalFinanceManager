package com.personalfinancemanager.service.sms;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.domain.entity.UserSettingsEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.sms.account-sid}")
    private String accountSid;

    @Value("${spring.sms.auth-token}")
    private String authToken;

    @Value("${spring.sms.number}")
    private String number;

    public void sendSms(UserEntity user, Integer smsDays, List<InvoiceEntity> invoices) {
        Twilio.init(accountSid, authToken);

        PhoneNumber toNumber = new PhoneNumber("+40760330010");
        PhoneNumber fromNumber = new PhoneNumber(number);
        String body = buildSmsBody(user, smsDays, invoices);

        Message message = Message
                .creator(toNumber, fromNumber, body)
                .create();

        System.out.println(message.getSid());
    }

    public String buildSmsBody(UserEntity user, Integer smsDays, List<InvoiceEntity> invoices) {
        String body = String.format("Hello %s!\nYou have %d invoices that are due in %d days:\n\n",
                user.getFirstname(), invoices.size(), smsDays);

        for (InvoiceEntity invoice : invoices) {
            body += String.format("%s - %.2f RON - %s\n",
                    invoice.getRetailer(), invoice.getAmount(), invoice.getType().name());
        }

        return body;
    }
}
