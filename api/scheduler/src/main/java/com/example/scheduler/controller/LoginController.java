package com.example.scheduler.controller;

import com.example.scheduler.DTOs.LoginDTO;
import com.example.scheduler.DTOs.TokenDTO;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.repositories.UserRepository;
import com.example.scheduler.repositories.TokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
     * @param loginData holds username and password of the user trying to log in
     * @return the token the client uses to authenticate during request
     * @throws LoginFailedException if username or password do not match the user saved in the database
     * User login to validate request from the client
     */
    @PostMapping("/login")
    TokenDTO login(@RequestBody LoginDTO loginData) throws LoginFailedException{
        UsersEntity user = userRepository.findUserByUsername(loginData.getUsername()).orElseThrow(LoginFailedException::new);
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        if(!b.matches(loginData.getPassword(), user.getHashedpw())){
            throw new LoginFailedException();
        }
        TokensEntity token = new TokensEntity(user.getId());
        tokenRepository.save(token);

        return new TokenDTO( token.getUserId(), token.getToken());
    }

    /**
     * @param token holds the login token of users logging out
     * @return Response that logging out was successful or unsuccessful
     * User logout so client can not make forbidden requests until new user logs in
     */
    @DeleteMapping("/login")
    ResponseEntity<String> logout(@RequestBody TokenDTO token){
        if(tokenRepository.isValid(token.getTokenString(), token.getUserID())){
            tokenRepository.deleteById(token.getTokenString());
            return ResponseEntity.ok().body("You were successfully logged out");
        }
        else return ResponseEntity.badRequest().body("Could not match token to correct login data");

    }
}
