package com.example.notification_system.Service;


import com.example.notification_system.Config.RabbitMQConfig;
import com.example.notification_system.Dto.EmailRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendToQueue(EmailRequestDTO dto){
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,dto);

    }
}
