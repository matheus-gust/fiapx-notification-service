package br.com.fiap.fiapx.notification.notification.infra.messaging;

import br.com.fiap.fiapx.notification.config.RabbitConfig;
import br.com.fiap.fiapx.notification.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.VIDEO_NOTIFICATION_QUEUE)
    public void consume(VideoNotificationMessage message) {
        log.info("Received notification for videoId={}", message.videoId());
        notificationService.notifyError(message);
    }
}
