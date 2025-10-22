package com.example.notification_system.Service;

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

    private final String EMAIL_QUEUE = "email-queue";

    // Step 1: Send email request to RabbitMQ queue
    public void sendEmailAsync(String toEmail, String subject, String body) {
        EmailLog emailLog = new EmailLog();
        emailLog.setToMail(toEmail);
        emailLog.setSubject(subject);
        emailLog.setBody(body);
        emailLog.setDate(LocalDate.now());
        emailLog.setStaus("Pending"); // initially pending
        emailLogRepository.save(emailLog);

        // send the email details to RabbitMQ queue
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, emailLog.getId());
    }

    // Step 2: Consume from RabbitMQ and send actual email
    @RabbitListener(queues = EMAIL_QUEUE)
    public void processEmailQueue(Long emailLogId) {
        EmailLog emailLog = emailLogRepository.findById(emailLogId).orElse(null);
        if (emailLog == null) return;

        Email from = new Email("petergwenstacy123@gmail.com");
        Email to = new Email(emailLog.getToMail());
        Content content = new Content("text/plain", emailLog.getBody());
        Mail mail = new Mail(from, emailLog.getSubject(), to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            emailLog.setStaus(response.getStatusCode() == 202 ? "Success" : "Failed");
            emailLogRepository.save(emailLog);
        } catch (IOException e) {
            emailLog.setStaus("Error: " + e.getMessage());
            emailLogRepository.save(emailLog);
        }
    }
}
