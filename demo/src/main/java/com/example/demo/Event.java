package com.example.demo;

import java.util.Objects;
import java.util.List;

public class Event {

    private Long id;
    private String name;

    private List<User> participants;
    private String date;
    private String duration;
    private String location;

    Event()  {}

    Event(String name, List<User> participants, String date, String duration, String location ){
        this.name = name;
        this.participants = participants;
        this.date = date;
        this.duration = duration;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Event))
            return false;
        Event event = (Event)o;
        return Objects.equals(this.id, event.id) && Objects.equals(this.name, event.name)
                && Objects.equals(this.date, event.date)
                && Objects.equals(this.duration, event.duration)
                && Objects.equals(this.location, event.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name='" + this.name +
                '\'' + ", date='" + this.date +
                '\''+  ", duration='" + this.duration +
                '\'' + ", location='" + this.location +
                '\''+ '}';
    }
}
