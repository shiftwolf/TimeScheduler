package com.example.scheduler.controller;

import com.example.scheduler.DTOs.LoginDTO;
import com.example.scheduler.DTOs.TokenDTO;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.repositories.UserRepository;
import com.example.scheduler.repositories.TokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @author Max
 * @version 1.0
 * Entry-point for all user login related REST-API requests
 */
@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     * @param userRepository Repository that holds all registered Users (injected)
     * @param tokenRepository Repository that holds all tokens of currently active Users (injected)
     *  using constructor injection (Dependency injection)
     *  meaning this constructor is typically not used manually.
     */
    LoginController(UserRepository userRepository,
                    TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * User login to validate request from the client by receiving DTO containing username and the password
     * @param loginData holds username and password of the user trying to log in
     * @return the token the client uses to authenticate during request
     * @throws LoginFailedException if username or password do not match the user saved in the database
     */
    @PostMapping("/login")
    TokenDTO login(@RequestBody LoginDTO loginData) throws LoginFailedException{
        //Checks validity of the given username
        UsersEntity user = userRepository.findUserByUsername(loginData.getUsername()).orElseThrow(LoginFailedException::new);
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        //Checks validity of the given password
        if(!b.matches(loginData.getPassword(), user.getHashedpw())){
            throw new LoginFailedException();
        }
        TokensEntity token = new TokensEntity(user.getId());
        token = tokenRepository.save(token);

        return new TokenDTO(token.getUserId(), token.getToken());
    }

    /**
     * User logout so client can not make forbidden requests until new user logs in
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     * @return Response that logging out was successful or unsuccessful
     */
    @DeleteMapping("/login")
    ResponseEntity<String> logout(@RequestHeader("userId") Long userId,
                                  @RequestHeader("token") String token){
        if(tokenRepository.isValid(token, userId) && tokenRepository.findByToken(token).isPresent()){
            TokensEntity e = tokenRepository.findByToken(token).get();
            tokenRepository.delete(e);
            return ResponseEntity.ok().body("You were successfully logged out");
        }
        else return ResponseEntity.badRequest().body("Could not match token to correct login data");

    }
}
