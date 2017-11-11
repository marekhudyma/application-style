package com.marekhudyma.testcontainers.config;

import com.marekhudyma.testcontainers.queue.ListenerErrorHandler;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {

    public static final String EXCHANGE_NAME = "com.marekhudyma.testcontainers.exchange1";

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange applicationEngineExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                         ListenerErrorHandler listenerErrorHandler) {
        return createContainerFactory(connectionFactory, listenerErrorHandler);
    }

    private RabbitListenerContainerFactory createContainerFactory(ConnectionFactory connectionFactory,
                                                                  ListenerErrorHandler listenerErrorHandler) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMaxConcurrentConsumers(1);
        factory.setConcurrentConsumers(1);
        factory.setErrorHandler(listenerErrorHandler);
        // factory.setAdviceChain(RetryInterceptorBuilder.stateless().retryPolicy(new NeverRetryPolicy()).build());
        factory.setMissingQueuesFatal(false);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

}
