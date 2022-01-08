package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {

    @GetMapping("/events")
    List<Event> all() {
        //TODO: return event data from db
        return null;
    }

    @PostMapping("/events")
    Event newEvent(@RequestBody Event newEvent) {
        //TODO: create new event data in db
        return null;
    }

    @GetMapping("/events/{id}")
    Event one(@PathVariable Long id) {
        // TODO: get Event by ID
        return null;
    }

    @DeleteMapping("/events/{id}")
    void deleteEmployee(@PathVariable Long id) {
        //TODO: delete Event in db
    }
}
