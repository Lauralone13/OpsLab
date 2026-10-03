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

    @Test
    void shouldReturnEventsWithWantedSeverity() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event1 = new SystemEvent(service, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event2 = new SystemEvent(service, Severity.WARNING, "WarningMessage", timestamp);
        SystemEvent event3 = new SystemEvent(service, Severity.INFO, "InformationMessage", timestamp);
        SystemEvent event4 = new SystemEvent(service, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event5 = new SystemEvent(service, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event6 = new SystemEvent(service, Severity.WARNING, "WarningMessage", timestamp);

        repository.addEvent(event1);
        repository.addEvent(event2);
        repository.addEvent(event3);
        repository.addEvent(event4);
        repository.addEvent(event5);
        repository.addEvent(event6);

        List<SystemEvent> errorEvents = repository.findEventsBySeverity(Severity.ERROR);

        assertEquals(3, errorEvents.size());
        assertTrue(errorEvents.contains(event1));
        assertTrue(errorEvents.contains(event4));
        assertTrue(errorEvents.contains(event5));
    }

    @Test
    void shouldFindNothingAndReturnAnEmptyListWhenWantedSeverityIsNotFound() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event1 = new SystemEvent(service, Severity.WARNING, "WarningMessage", timestamp);
        SystemEvent event2 = new SystemEvent(service, Severity.INFO, "InformationMessage", timestamp);

        repository.addEvent(event1);
        repository.addEvent(event2);

        List<SystemEvent> errorEvents =
                repository.findEventsBySeverity(Severity.ERROR);

        assertTrue(errorEvents.isEmpty());
    }

    @Test
    void shouldRejectNullSeverityWhenFiltering() {
        SystemEventRepository repository = new SystemEventRepository();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.findEventsBySeverity(null)
        );

        assertEquals("Severity must not be null", exception.getMessage());
    }

    @Test
    void shouldProtectEventsFoundBySeverityFromExternalModification() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event = new SystemEvent(service, Severity.ERROR, "Database connection failed", timestamp);

        repository.addEvent(event);
        List<SystemEvent> returnedEvents = repository.findEventsBySeverity(Severity.ERROR);

        assertEquals(1, returnedEvents.size());

        assertThrows(
                UnsupportedOperationException.class,
                returnedEvents::clear
        );

        assertEquals(1, repository.findEventsBySeverity(Severity.ERROR).size());
    }

    @Test
    void shouldReturnEventsForWantedService() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService paymentService = new MonitoredService("payment-service", "Processes customer payments");
        MonitoredService otherService = new MonitoredService("other-service", "Other customer service");

        SystemEvent event1 = new SystemEvent(paymentService, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event2 = new SystemEvent(paymentService, Severity.WARNING, "WarningMessage", timestamp);
        SystemEvent event3 = new SystemEvent(paymentService, Severity.INFO, "InformationMessage", timestamp);
        SystemEvent event4 = new SystemEvent(otherService, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event5 = new SystemEvent(otherService, Severity.ERROR, "ErrorMessage", timestamp);
        SystemEvent event6 = new SystemEvent(otherService, Severity.WARNING, "WarningMessage", timestamp);

        repository.addEvent(event1);
        repository.addEvent(event2);
        repository.addEvent(event3);
        repository.addEvent(event4);
        repository.addEvent(event5);
        repository.addEvent(event6);

        MonitoredService searchedService = new MonitoredService("payment-service", "Completely different description");

        List<SystemEvent> returnedEvents = repository.findEventsByService(searchedService);

        assertEquals(3, returnedEvents.size());
        assertTrue(returnedEvents.contains(event1));
        assertTrue(returnedEvents.contains(event2));
        assertTrue(returnedEvents.contains(event3));
    }

    @Test
    void shouldFindNothingAndReturnAnEmptyListWhenWantedServiceIsNotFound() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService otherService = new MonitoredService("other-service", "other customer service");
        SystemEvent event1 = new SystemEvent(otherService, Severity.WARNING, "WarningMessage", timestamp);
        SystemEvent event2 = new SystemEvent(otherService, Severity.INFO, "InformationMessage", timestamp);

        repository.addEvent(event1);
        repository.addEvent(event2);

        MonitoredService searchedService = new MonitoredService("payment-service", "Completely different description");

        List<SystemEvent> returnedEvents =
                repository.findEventsByService(searchedService);

        assertTrue(returnedEvents.isEmpty());
    }

    @Test
    void shouldRejectNullServiceWhenFiltering() {
        SystemEventRepository repository = new SystemEventRepository();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.findEventsByService(null)
        );

        assertEquals("Service must not be null", exception.getMessage());
    }

    @Test
    void shouldProtectEventsFoundByServiceFromExternalModification() {
        SystemEventRepository repository = new SystemEventRepository();
        LocalDateTime timestamp = LocalDateTime.of(2026, 9, 20, 20, 35);
        MonitoredService service = new MonitoredService("payment-service", "Processes customer payments");
        SystemEvent event = new SystemEvent(service, Severity.ERROR, "Database connection failed", timestamp);

        repository.addEvent(event);

        MonitoredService searchedService = new MonitoredService("payment-service", "Completely different description");

        List<SystemEvent> returnedEvents = repository.findEventsByService(searchedService);

        assertEquals(1, returnedEvents.size());

        assertThrows(
                UnsupportedOperationException.class,
                returnedEvents::clear
        );

        assertEquals(1, repository.findEventsByService(searchedService).size());
    }

    @Test
    void shouldReturnEventsSortedChronologically() {
        SystemEventRepository repository = new SystemEventRepository();
        MonitoredService paymentService = new MonitoredService("payment-service", "Processes customer payments");
        MonitoredService otherService = new MonitoredService("other-service", "Other customer service");

        SystemEvent event1 = new SystemEvent(paymentService, Severity.ERROR, "ErrorMessage", LocalDateTime.of(2026, 9, 20, 10, 6));
        SystemEvent event2 = new SystemEvent(otherService, Severity.WARNING, "WarningMessage", LocalDateTime.of(2026, 9, 13, 14, 35));
        SystemEvent event3 = new SystemEvent(paymentService, Severity.INFO, "InformationMessage", LocalDateTime.of(2026, 9, 13, 21, 45));
        SystemEvent event4 = new SystemEvent(otherService, Severity.ERROR, "ErrorMessage", LocalDateTime.of(2026, 8, 27, 17, 26));

        repository.addEvent(event1);
        repository.addEvent(event2);
        repository.addEvent(event3);
        repository.addEvent(event4);

        List<SystemEvent> sortedEvents = repository.getEventsChronologically();

        assertEquals(4, sortedEvents.size());
        assertEquals(event4, sortedEvents.get(0));
        assertEquals(event2, sortedEvents.get(1));
        assertEquals(event3, sortedEvents.get(2));
        assertEquals(event1, sortedEvents.get(3));
    }

    @Test
    void shouldNotChangeStoredEventOrderWhenReturningEventsChronologically() {
        SystemEventRepository repository = new SystemEventRepository();
        MonitoredService paymentService = new MonitoredService("payment-service", "Processes customer payments");
        MonitoredService otherService = new MonitoredService("other-service", "Other customer service");

        SystemEvent event1 = new SystemEvent(paymentService, Severity.ERROR, "ErrorMessage", LocalDateTime.of(2026, 9, 20, 10, 6));
        SystemEvent event2 = new SystemEvent(otherService, Severity.WARNING, "WarningMessage", LocalDateTime.of(2026, 9, 13, 14, 35));
        SystemEvent event3 = new SystemEvent(paymentService, Severity.INFO, "InformationMessage", LocalDateTime.of(2026, 9, 13, 21, 45));
        SystemEvent event4 = new SystemEvent(otherService, Severity.ERROR, "ErrorMessage", LocalDateTime.of(2026, 8, 27, 17, 26));

        repository.addEvent(event1);
        repository.addEvent(event2);
        repository.addEvent(event3);
        repository.addEvent(event4);

        repository.getEventsChronologically();

        List<SystemEvent> storedEvents = repository.getAllEvents();

        assertEquals(4, storedEvents.size());
        assertEquals(event1, storedEvents.get(0));
        assertEquals(event2, storedEvents.get(1));
        assertEquals(event3, storedEvents.get(2));
        assertEquals(event4, storedEvents.get(3));
    }
}
