package com.example.scheduler.controller;

import java.util.*;

import com.example.scheduler.DTOs.NewUserDTO;
import com.example.scheduler.DTOs.UserDTO;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.EmailAlreadyExistsException;
import com.example.scheduler.exceptions.UserNotFoundException;
import com.example.scheduler.exceptions.UsernameTakenException;
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
    List<UserDTO> all() {
        List<UserDTO> dtos = new ArrayList<>();
        for (UsersEntity e : userRepository.findAll()) {
            UserDTO dto = new UserDTO(e.getId(), e.getUsername(), e.getName(), e.getEmail());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * @param newUser Converts User DTO to entity and saves it to the database
     * @throws UsernameTakenException if username is already taken
     * @throws EmailAlreadyExistsException if email is already taken
     */
    @PostMapping("/users")
    void newUser(@RequestBody NewUserDTO newUser) throws UsernameTakenException, EmailAlreadyExistsException {
        Optional.ofNullable(userRepository.findUserByUsername(newUser.getUsername())).ifPresent(arg -> {
            throw new UsernameTakenException(newUser.getUsername());
        });

        Optional.ofNullable(userRepository.findUserByEmail(newUser.getEmail())).ifPresent(arg -> {
            throw new EmailAlreadyExistsException(newUser.getEmail());
        });

        UsersEntity user = new UsersEntity(
                newUser.getEmail(), newUser.getUsername(), newUser.getName(), newUser.getPassword()
        );

        userRepository.save(user);
    }

    @GetMapping("/users/{id}")
    UserDTO one(@PathVariable Long id) throws UserNotFoundException {
        if(userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException(id);
        }

        UsersEntity user = userRepository.findById(id).get();

        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    @DeleteMapping("/users/{id}")
    void deleteEmployee(@PathVariable Long id) {
       userRepository.deleteById(id);
    }
}


