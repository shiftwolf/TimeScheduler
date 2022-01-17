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

    @PostMapping("/users")
    void newUser(@RequestBody NewUserDTO newUser) throws UsernameTakenException, EmailAlreadyExistsException {

        String checkName = userRepository.findUserByUsername(newUser.getUsername()).getUsername();
        if(checkName != null){
            throw new UsernameTakenException(checkName);
        }
        String checkEmail = userRepository.findUserByEmail(newUser.getEmail()).getEmail();
        if(checkEmail != null){
            throw  new EmailAlreadyExistsException(checkEmail);
        }

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


