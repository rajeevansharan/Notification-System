package com.example.notification_system.Controller;

import com.example.notification_system.Service.SmsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

@PostMapping("/send")
    public String sendSms(@RequestParam String toNumber,@RequestParam String message) {

      return  smsService.sendSms(toNumber,message);
    }
}



