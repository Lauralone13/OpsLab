package dev.opslab.domain;

import java.time.LocalDateTime;

public class SystemEvent {

    private final String service;
    private final Severity severity;
    private final String message;
    private final LocalDateTime timestamp;

    public SystemEvent(String service, Severity severity, String message, LocalDateTime timestamp) {

        if (service == null || service.isBlank()) {
            throw new IllegalArgumentException("Service must not be blank");
        }

        if (severity == null) {
            throw new IllegalArgumentException("Severity must not be null");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message must not be blank");
        }

        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp must not be null");
        }

        this.service = service ;
        this.severity = severity;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
