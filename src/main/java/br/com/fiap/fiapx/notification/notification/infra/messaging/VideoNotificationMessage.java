package br.com.fiap.fiapx.notification.notification.infra.messaging;

import java.util.UUID;

public record VideoNotificationMessage(
        UUID videoId,
        UUID userId,
        String userEmail,
        String errorMessage
) {}
