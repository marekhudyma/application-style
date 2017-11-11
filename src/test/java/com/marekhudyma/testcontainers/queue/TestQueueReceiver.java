package com.marekhudyma.testcontainers.queue;

import com.marekhudyma.testcontainers.config.RabbitConsumerQueueConfig;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j
@Component
public class TestQueueReceiver {

    private Queue<MessageDto> accounts = new ConcurrentLinkedQueue<>();

    @RabbitListener(containerFactory = "rabbitListenerContainerFactory", queues = RabbitConsumerQueueConfig.QUEUE_NAME)
    public void listen(@Payload MessageDto messageDto) {
        log.info("Received account");
        accounts.add(messageDto);
    }

    public void clean() {
        accounts.clear();
    }

    public Collection<MessageDto> getMessages() {
        return ImmutableList.copyOf(accounts);
    }

}