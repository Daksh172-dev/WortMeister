package com.wortmeister.common.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String AI_JOBS_EXCHANGE = "ai.jobs";
    public static final String NOTIFICATION_EXCHANGE = "notification.jobs";
    public static final String AUDIT_EXCHANGE = "audit.events";

    @Bean
    DirectExchange aiJobsExchange() {
        return new DirectExchange(AI_JOBS_EXCHANGE, true, false);
    }

    @Bean
    DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    TopicExchange auditExchange() {
        return new TopicExchange(AUDIT_EXCHANGE, true, false);
    }

    @Bean
    Queue aiJobQueue() {
        return QueueBuilder.durable("ai.jobs.requests").deadLetterExchange("ai.jobs.dlx").build();
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable("notification.jobs.delivery").deadLetterExchange("notification.jobs.dlx").build();
    }
}
