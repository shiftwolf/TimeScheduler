/* User
 * Version: 1
 * 30.12.2021
 * Hendrik
 */

package Model;

import java.util.ArrayList;
import java.util.Date;

public class User {

    // Attributes
    private final int id;
    private final Date created_at;
    private final String email;
    private final String username;
    private final String name;
    private String password;
    private ArrayList<Event> events;

    // Constructor
    public User(int id, String email, String username, String name, String password, ArrayList<Event> events) {
        this.id = id;
        this.created_at = new Date();;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.events = events;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    // Methods
    public void addEvent(Event event){
        events.add(event);
    }
}
