package com.example.notification_system.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {


    public static final String QUEUE_NAME = "emailQueue";

    @Bean
    public Queue emailQueue()
    {
        return new Queue(QUEUE_NAME,true);
    }
}
