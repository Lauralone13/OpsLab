package dev.opslab.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SystemEventTest {

    @Test
    void shouldCreateSystemEventWithValidData() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act
        SystemEvent systemEvent = new SystemEvent("payment-service", Severity.ERROR, "Database connection failed", timestamp);

        // Assert
        assertEquals("payment-service", systemEvent.getService());
        assertEquals(Severity.ERROR, systemEvent.getSeverity());
        assertEquals("Database connection failed", systemEvent.getMessage());
        assertEquals(timestamp, systemEvent.getTimestamp());
    }

    @Test
    void shouldRejectBlankService() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent("", Severity.ERROR, "Database connection failed", timestamp));
    }

    @Test
    void shouldRejectNullService() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent(null, Severity.ERROR, "Database connection failed", timestamp)
        );

        assertEquals("Service must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectNullSeverity() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent("payment-service", null, "Database connection failed", timestamp));
    }

    @Test
    void shouldRejectBlankMessage() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent("payment-service", Severity.ERROR, "", timestamp));
    }

    @Test
    void shouldRejectNullMessage() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent("payment-service", Severity.ERROR, null, timestamp));
    }

    @Test
    void shouldRejectNullTimestamp() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemEvent("payment-service", Severity.WARNING, "Database connection failed", null));
    }
}