package com.marekhudyma.testcontainers.queue;

import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;


class QueueIntegrationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private QueuePublisher queuePublisher;

    @Autowired
    private TestQueueReceiver testQueueReceiver;

    @Test
    void shouldSendMessage() throws Exception {
        MessageDto messageDto = new MessageDto(1L);

        queuePublisher.publish(messageDto);

        await().atMost(120, SECONDS).until(() -> {
            Optional<MessageDto> accountOptional = testQueueReceiver.getMessages().stream()
                    .filter(a -> a.equals(messageDto))
                    .findFirst();

            return accountOptional.isPresent();
        });
    }

}
