package Entities;

import jakarta.persistence.*;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDate;

@Table(name="events")
@Entity
public class Event implements DutyDay {
    @Override
    public boolean isRegular() {
        return false;
    }

    @Id
    private LocalDate date;

    private String name;

    @Column(name = "notification_needed")
    @Convert(converter = NumericBooleanConverter.class)
    private boolean notificationNeeded;

    private String description;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotificationNeeded() {
        return notificationNeeded;
    }

    public void setNotificationNeeded(boolean notificationNeeded) {
        this.notificationNeeded = notificationNeeded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event() {}

    public Event(LocalDate date, String reason) {
        this.date = date;
        this.name = reason;
    }

    @Override
    public String toString() {
        return "Событие: " + name;
    }
}
