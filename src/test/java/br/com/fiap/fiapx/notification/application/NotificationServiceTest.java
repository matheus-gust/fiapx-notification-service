package br.com.fiap.fiapx.notification.application;

import br.com.fiap.fiapx.notification.application.messages.NotificationMessage;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock private JavaMailSender mailSender;
    @InjectMocks private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@fiapx.com");
    }

    @Test
    void notifyError_shouldSendEmailToUser() {
        NotificationMessage message = new NotificationMessage(
                UUID.randomUUID(), "user@test.com", "FFmpeg falhou");

        notificationService.notifyError(message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getTo()).contains("user@test.com");
        assertThat(sent.getFrom()).isEqualTo("noreply@fiapx.com");
        assertThat(sent.getSubject()).contains("Erro");
        assertThat(sent.getText()).contains("FFmpeg falhou");
    }

    @Test
    void notifyError_shouldNotThrowWhenMailFails() {
        NotificationMessage message = new NotificationMessage(
                UUID.randomUUID(), "user@test.com", "erro");
        doThrow(new RuntimeException("SMTP down")).when(mailSender).send(any(SimpleMailMessage.class));

        org.assertj.core.api.Assertions.assertThatCode(() -> notificationService.notifyError(message))
                .doesNotThrowAnyException();
    }

    @Test
    void notifyError_shouldIncludeVideoIdInEmail() {
        UUID videoId = UUID.randomUUID();
        NotificationMessage message = new NotificationMessage(videoId, "user@test.com", "erro");

        notificationService.notifyError(message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue().getText()).contains(videoId.toString());
    }
}
