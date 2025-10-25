package com.example.notification_system.Service;


import com.example.notification_system.Config.RabbitMQConfig;
import com.example.notification_system.Entity.EmailLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendToQueue(EmailLog emailLog ){
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,emailLog);

    }
}
