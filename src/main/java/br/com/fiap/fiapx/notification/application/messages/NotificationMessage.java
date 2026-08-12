package br.com.fiap.fiapx.notification.application.messages;

import java.util.UUID;

public record NotificationMessage(UUID videoId, String userEmail, String errorMessage) {}
