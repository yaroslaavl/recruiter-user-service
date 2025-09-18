package org.yaroslaavl.userservice.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaroslaavl.userservice.broker.dto.NotificationDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    public String exchange;

    @Value("${rabbitmq.user.queues.start.routing-key}")
    public String routingKey;

    public void publishUserEvent(NotificationDto notificationDto) {
        log.info("Publishing user event for notification {}", notificationDto);
        rabbitTemplate.convertAndSend(exchange, routingKey, notificationDto);
    }
}
