package dev.opslab.repository;

import dev.opslab.domain.SystemEvent;
import java.util.ArrayList;
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
}
