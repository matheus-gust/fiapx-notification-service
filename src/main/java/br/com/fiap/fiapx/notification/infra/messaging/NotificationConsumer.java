package br.com.fiap.fiapx.notification.infra.messaging;

import br.com.fiap.fiapx.config.RabbitConfig;
import br.com.fiap.fiapx.notification.application.NotificationService;
import br.com.fiap.fiapx.notification.application.messages.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void consume(NotificationMessage message) {
        log.info("Received notification request for video {} / user {}", message.videoId(), message.userEmail());
        notificationService.notifyError(message);
    }
}
