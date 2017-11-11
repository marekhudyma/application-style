package com.marekhudyma.testcontainers.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConsumerQueueConfig {

    public static final String QUEUE_NAME = "com.marekhudyma.testcontainers.queue1";

    @Bean
    public Queue stateUpdateInfoQueue() {
        return new Queue(QUEUE_NAME);
    }

}
