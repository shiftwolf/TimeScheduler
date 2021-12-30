/* Event
 * Version: 1
 * 30.12.2021
 * Hendrik
 */

package Model;

import java.util.Date;
import java.util.ArrayList;

public class Event {

    public static enum priorities {
        GREEN, YELLOW, RED
    }

    // Attributes
    private final int id;
    private final Date created_at;
    private final String name;
    private Date date;
    private Date duration;
    private String location;
    private String description;
    private priorities priority;
    public ArrayList<User> participants;

    // Constructor
    public Event(int id, String name, Date date, Date duration, String location, String description, priorities priority, ArrayList<User> participants) {
        this.id = id;
        this.created_at = new Date();
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.participants = participants;
        participants = new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Date getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public priorities getPriority() {
        return priority;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    // Setters
    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(priorities priority) {
        this.priority = priority;
    }

    // Methods
    public void addParticipants(User newParticipant) {
        participants.add(newParticipant);
    }

}
