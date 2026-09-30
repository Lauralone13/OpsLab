package dev.opslab.repository;

import dev.opslab.domain.MonitoredService;
import dev.opslab.domain.Severity;
import dev.opslab.domain.SystemEvent;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemEventRepositoryTest {

    @Test
    void shouldHaveNoEventsWhenRepositoryIsCreated() {
        SystemEventRepository repository = new SystemEventRepository();

        assertTrue(repository.getAllEvents().isEmpty());
    }

    @Test
    void shouldRejectAddingNullToEvents() {
        SystemEventRepository repository = new SystemEventRepository();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.addEvent(null)
        );

        assertEquals("Event must not be null", exception.getMessage());
    }

    @Test
    void shouldAddNewValidEvent() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event = new SystemEvent(service, Severity.ERROR, "Database connection failed", timestamp);

        repository.addEvent(event);
        List<SystemEvent> events = repository.getAllEvents();

        assertEquals(1, events.size());
        assertSame(event, events.getFirst());
    }

    @Test
    void shouldReturnThreeEvents() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event1 = new SystemEvent(service, Severity.ERROR, "Database connection failed", timestamp);
        SystemEvent event2 = new SystemEvent(service, Severity.WARNING, "WarningMessage", timestamp);
        SystemEvent event3 = new SystemEvent(service, Severity.INFO, "InformationMessage", timestamp);

        repository.addEvent(event1);
        repository.addEvent(event2);
        repository.addEvent(event3);

        List<SystemEvent> eventList = repository.getAllEvents();

        assertEquals(3, eventList.size());
        assertTrue(eventList.contains(event1));
        assertTrue(eventList.contains(event2));
        assertTrue(eventList.contains(event3));
    }

    @Test
    void shouldProtectEventsFromExternalModification() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event = new SystemEvent(service, Severity.ERROR, "Database connection failed", timestamp);

        repository.addEvent(event);
        List<SystemEvent> returnedEvents = repository.getAllEvents();

        assertThrows(
                UnsupportedOperationException.class,
                returnedEvents::clear
        );

        assertEquals(1, repository.getAllEvents().size());
    }
}
