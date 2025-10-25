package com.example.notification_system.Service;

import com.example.notification_system.Config.RabbitMQConfig;
import com.example.notification_system.Entity.EmailLog;
import com.example.notification_system.Repository.EmailLogRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public String sendEmail(String toEmail, String subject, String body) {
        Email from = new Email("petergwenstacy123@gmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        EmailLog emailLog = new EmailLog();
        emailLog.setToMail(toEmail);
        emailLog.setSubject(subject);
        emailLog.setBody(body);
        emailLog.setDate(LocalDate.now());

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            emailLog.setStaus(response.getStatusCode() == 202 ? "Success" : "Failed");
            emailLogRepository.save(emailLog);

            return response.getStatusCode() + " " + response.getBody();
        } catch (IOException e) {
            emailLog.setStaus("Error" + e.getMessage());
            emailLogRepository.save(emailLog);
            return "Failed to send email:" + e.getMessage();
        }
    }
}
