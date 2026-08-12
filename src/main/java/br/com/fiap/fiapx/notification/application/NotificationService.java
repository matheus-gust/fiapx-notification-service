package br.com.fiap.fiapx.notification.application;

import br.com.fiap.fiapx.notification.application.messages.NotificationMessage;
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

    @Value("${notification.from-email:noreply@fiapx.com}")
    private String fromEmail;

    public void notifyError(NotificationMessage message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            mail.setTo(message.userEmail());
            mail.setSubject("FIAP X - Erro no processamento do seu vídeo");
            mail.setText(String.format(
                    "Olá!\n\nOcorreu um erro ao processar seu vídeo (ID: %s).\n\nDetalhe: %s\n\nTente novamente em breve.\n\nEquipe FIAP X",
                    message.videoId(), message.errorMessage()
            ));
            mailSender.send(mail);
            log.info("Error notification sent to {}", message.userEmail());
        } catch (Exception e) {
            log.error("Failed to send notification to {}: {}", message.userEmail(), e.getMessage());
        }
    }
}
