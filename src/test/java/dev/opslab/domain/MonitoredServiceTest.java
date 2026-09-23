package dev.opslab.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonitoredServiceTest {

    @Test
    void shouldCreateMonitoredServiceWithValidData() {
        // Arrange & Act
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        // Assert
        assertEquals("payment-service", service.getName());
        assertEquals("Processes customer payments", service.getDescription());
        assertEquals(ServiceStatus.UNKNOWN, service.getStatus());
    }

    @Test
    void shouldCreateMonitoredServiceWithDescriptionIsNull() {
        // Arrange & Act
        MonitoredService service = new MonitoredService("payment-service", null);

        // Assert
        assertEquals("payment-service", service.getName());
        assertNull(service.getDescription());
        assertEquals(ServiceStatus.UNKNOWN, service.getStatus());
    }

    @Test
    void shouldRejectNullName() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new MonitoredService(null, "Processes customer payments"));
    }

    @Test
    void shouldRejectBlankName() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new MonitoredService(" ", "Processes customer payments"));
    }

    @Test
    void shouldRejectBlankDescription() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new MonitoredService("payment-service", ""));
    }

    @Test
    void shouldMarkServiceStatusAsAvailable() {
        // Arrange
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        assertEquals(ServiceStatus.UNKNOWN, service.getStatus());

        // Act
        service.markAvailable();

        // Assert
        assertEquals(ServiceStatus.AVAILABLE, service.getStatus());
    }

    @Test
    void shouldMarkServiceStatusAsUnavailable() {
        // Arrange
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        assertEquals(ServiceStatus.UNKNOWN, service.getStatus());

        // Act
        service.markUnavailable();

        // Assert
        assertEquals(ServiceStatus.UNAVAILABLE, service.getStatus());
    }

    @Test
    void shouldUpdateDescription() {
        // Arrange
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        assertEquals("Processes customer payments", service.getDescription());

        // Act
        service.setDescription("Processes customer payment transactions");

        // Assert
        assertEquals("Processes customer payment transactions", service.getDescription());
    }

    @Test
    void shouldAllowDescriptionToBeSetToNull() {
        // Arrange
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        assertEquals("Processes customer payments", service.getDescription());

        // Act
        service.setDescription(null);

        // Assert
        assertNull(service.getDescription());
    }

    @Test
    void shouldRejectBlankDescriptionOnUpdate() {
        // Arrange
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");

        assertEquals("Processes customer payments", service.getDescription());

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.setDescription("  "));
    }
}
