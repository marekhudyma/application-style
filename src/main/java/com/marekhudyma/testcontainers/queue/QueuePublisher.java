package com.marekhudyma.testcontainers.queue;

import com.marekhudyma.testcontainers.config.RabbitConsumerQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueuePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(MessageDto messageDto) {
        rabbitTemplate.convertAndSend(RabbitConsumerQueueConfig.QUEUE_NAME, messageDto);
    }
}
