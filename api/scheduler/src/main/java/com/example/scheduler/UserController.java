package com.example.scheduler;

import java.util.List;

import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.repositories.UserRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    List<User> all() {
       //TODO: return user data from db
        return null;
    }

    @PostMapping("/users")
    void newUser(@RequestBody User newUser) {
        UsersEntity user = new UsersEntity(
                newUser.email, "username", newUser.name, newUser.password
        );
        userRepository.save(user);
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


