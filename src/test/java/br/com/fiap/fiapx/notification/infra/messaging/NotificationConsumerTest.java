package br.com.fiap.fiapx.notification.infra.messaging;

import br.com.fiap.fiapx.notification.application.NotificationService;
import br.com.fiap.fiapx.notification.application.messages.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock private NotificationService notificationService;
    @InjectMocks private NotificationConsumer consumer;

    @Test
    void consume_shouldDelegateToNotificationService() {
        NotificationMessage message = new NotificationMessage(
                UUID.randomUUID(), "user@test.com", "Erro no processamento");

        consumer.consume(message);

        verify(notificationService).notifyError(message);
    }
}
