package com.scalable.notifications.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "enrollments.exchange";
    public static final String QUEUE = "notification.queue";
    public static final String ROUTING_KEY = "enrollment.confirmed";

    @Bean
    public TopicExchange enrollmentExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE, true, false, false);
    }

    @Bean
    public Binding enrollmentBinding(Queue notificationQueue,
                                     TopicExchange enrollmentExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(enrollmentExchange)
                .with(ROUTING_KEY);
    }

    @SuppressWarnings("removal")
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}