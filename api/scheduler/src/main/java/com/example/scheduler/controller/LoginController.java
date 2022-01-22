package com.example.scheduler.controller;

import com.example.scheduler.DTOs.LoginDTO;
import com.example.scheduler.DTOs.TokenDTO;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserRepository userRepository;

    LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    TokenDTO login(@RequestBody LoginDTO loginData) throws LoginFailedException{
        UsersEntity user = userRepository.findUserByUsername(loginData.getUsername());
        if(user == null){
            throw new LoginFailedException();
        }
        if(!user.getHashedpw().equals(loginData.getPassword())){
            throw new LoginFailedException();
        }

        //TODO: create Token in DB

        return new TokenDTO(user.getId(), "NOT_FINNISHED_YET");
    }
}
