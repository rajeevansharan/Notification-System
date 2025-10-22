package com.example.notification_system.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone_number}")
    private String fromNumber;


    public String sendSms(String toNumber, String body) {

        Message message = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber),
                body
        ).create();
        return message.getSid();
    }
}
