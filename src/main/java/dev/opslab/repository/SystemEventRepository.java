package dev.opslab.repository;

import dev.opslab.domain.MonitoredService;
import dev.opslab.domain.Severity;
import dev.opslab.domain.SystemEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SystemEventRepository {

    private final List<SystemEvent> events = new ArrayList<>();

    public void addEvent(SystemEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }

        events.add(event);
    }

    public List<SystemEvent> getAllEvents() {
        return List.copyOf(events);
    }

    public List<SystemEvent> findEventsBySeverity(Severity severity) {
        if (severity == null) {
            throw new IllegalArgumentException("Severity must not be null");
        }

        return events.stream()
                .filter(event -> event.getSeverity() == severity)
                .toList();
    }

    public List<SystemEvent> findEventsByService(MonitoredService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service must not be null");
        }

        return events.stream()
                .filter(event -> event.getService().equals(service))
                .toList();
    }

    public List<SystemEvent> getEventsChronologically() {
        return events.stream()
                .sorted(Comparator.comparing(SystemEvent::getTimestamp))
                .toList();
    }
}
