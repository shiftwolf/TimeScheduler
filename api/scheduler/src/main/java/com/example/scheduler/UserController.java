package com.example.scheduler;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    List<User> all() {
       //TODO: return user data from db
        return null;
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        //TODO: create new user data in db
        return null;
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

       // TODO: get User by ID
        return null;
    }

    @DeleteMapping("/users/{id}")
    void deleteEmployee(@PathVariable Long id) {
       //TODO: delete User in db
    }
}


