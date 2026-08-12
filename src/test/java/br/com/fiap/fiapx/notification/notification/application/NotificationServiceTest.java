package br.com.fiap.fiapx.notification.notification.application;

import br.com.fiap.fiapx.notification.notification.infra.messaging.VideoNotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void notifyError_sendsEmail() {
        ReflectionTestUtils.setField(notificationService, "from", "noreply@fiapx.com");
        VideoNotificationMessage msg = new VideoNotificationMessage(
                UUID.randomUUID(), UUID.randomUUID(), "user@test.com", "FFmpeg error");

        notificationService.notifyError(msg);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getTo()).contains("user@test.com");
        assertThat(sent.getSubject()).contains("Erro");
        assertThat(sent.getText()).contains("FFmpeg error");
    }

    @Test
    void notifyError_skipsWhenNoEmail() {
        VideoNotificationMessage msg = new VideoNotificationMessage(
                UUID.randomUUID(), UUID.randomUUID(), null, "error");

        notificationService.notifyError(msg);

        verifyNoInteractions(mailSender);
    }
}
