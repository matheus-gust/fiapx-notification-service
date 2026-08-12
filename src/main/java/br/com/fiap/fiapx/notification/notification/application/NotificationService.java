package br.com.fiap.fiapx.notification.notification.application;

import br.com.fiap.fiapx.notification.notification.infra.messaging.VideoNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${notification.from:noreply@fiapx.com}")
    private String from;

    public void notifyError(VideoNotificationMessage message) {
        if (message.userEmail() == null || message.userEmail().isBlank()) {
            log.warn("No email for userId={}, skipping notification", message.userId());
            return;
        }
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(from);
            mail.setTo(message.userEmail());
            mail.setSubject("Erro no processamento do seu video - FIAP X");
            mail.setText(String.format(
                    "Ola,\n\nOcorreu um erro ao processar seu video (ID: %s).\n\nDetalhes: %s\n\nEquipe FIAP X",
                    message.videoId(), message.errorMessage()));
            mailSender.send(mail);
            log.info("Notification sent to {} for videoId={}", message.userEmail(), message.videoId());
        } catch (Exception e) {
            log.error("Failed to send notification for videoId={}: {}", message.videoId(), e.getMessage(), e);
        }
    }
}
