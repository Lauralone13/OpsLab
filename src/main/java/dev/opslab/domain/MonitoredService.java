package dev.opslab.domain;

public class MonitoredService {

    private final String name;
    private String description;
    private ServiceStatus status;

    public MonitoredService(String name, String description) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Service name must not be blank");
        }

        checkValidDescription(description);

        this.name = name ;
        this.description = description;
        this.status = ServiceStatus.UNKNOWN;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setDescription(String newDescription) {
        checkValidDescription(newDescription);
        this.description = newDescription;
    }

    public void markAvailable() {
        this.status = ServiceStatus.AVAILABLE;
    }

    public void markUnavailable() {
        this.status = ServiceStatus.UNAVAILABLE;
    }

    private void checkValidDescription(String newDescription) {
       if (newDescription != null && newDescription.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MonitoredService)) {
            return false;
        }

        MonitoredService other = (MonitoredService) obj;

        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
