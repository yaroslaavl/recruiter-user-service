package org.yaroslaavl.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    private final String userExchange;
    private final String userQueue;
    private final String userRoutingKey;
    private final String userDlxExchange;
    private final String userDlqRoutingKey;
    private final long ttl;
    private final long length;
    private final String userDlq;

    public RabbitMqConfig(
            @Value("${rabbitmq.user.exchange}") String userExchange,
            @Value("${rabbitmq.user.queues.start.name}") String userQueue,
            @Value("${rabbitmq.user.queues.start.routing-key}") String userRoutingKey,
            @Value("${rabbitmq.user.queues.start.arguments.x-dead-letter-exchange}") String userDlxExchange,
            @Value("${rabbitmq.user.queues.start.arguments.x-dead-letter-routing-key}") String userDlqRoutingKey,
            @Value("${rabbitmq.user.queues.start.arguments.x-message-ttl}") long ttl,
            @Value("${rabbitmq.user.queues.start.arguments.x-max-length}") long length,
            @Value("${rabbitmq.user.queues.dead-letter.queue.name}") String userDlq
    ) {
        this.userExchange = userExchange;
        this.userQueue = userQueue;
        this.userRoutingKey = userRoutingKey;
        this.userDlxExchange = userDlxExchange;
        this.userDlqRoutingKey = userDlqRoutingKey;
        this.ttl = ttl;
        this.length = length;
        this.userDlq = userDlq;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(userExchange);
    }

    @Bean
    public DirectExchange userDlx() {
        return new DirectExchange(userDlxExchange);
    }

    @Bean
    public Queue userQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", userDlxExchange);
        args.put("x-dead-letter-routing-key", userDlqRoutingKey);
        args.put("x-message-ttl", ttl);
        args.put("x-max-length", length);
        return  new Queue(userQueue, true, false, false, args);
    }

    @Bean
    public Queue userDlqQueue() {
        Map<String, Object> args = new HashMap<>();
        return  new Queue(userDlq, true, false, false, args);
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder.bind(userQueue()).to(userExchange()).with(userRoutingKey);
    }

    @Bean
    public Binding userBindingDlx() {
        return BindingBuilder.bind(userDlqQueue()).to(userDlx()).with(userDlqRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
