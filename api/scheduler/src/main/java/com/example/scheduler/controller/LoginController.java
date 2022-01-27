package com.example.scheduler.controller;

import com.example.scheduler.DTOs.LoginDTO;
import com.example.scheduler.DTOs.TokenDTO;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.repositories.UserRepository;
import com.example.scheduler.repositories.TokenRepository;
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
     * @param userRepository Repository that holds all registered Users
     * @param tokenRepository Repository that holds all tokens of currently logged in Users
     *  using constructor injection (Dependency injection)
     *  meaning this constructor is typically not used manually.
     */
    LoginController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/login")
    TokenDTO login(@RequestBody LoginDTO loginData) throws LoginFailedException{
        UsersEntity user = userRepository.findUserByUsername(loginData.getUsername());
        if(user == null){
            throw new LoginFailedException();
        }
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        if(!b.matches(loginData.getPassword(), user.getHashedpw())){
            throw new LoginFailedException();
        }
        TokensEntity token = new TokensEntity(user.getId());
        tokenRepository.save(token);

        return new TokenDTO( token.getUserId(), token.getToken());
    }

    @DeleteMapping("/login")
    void logout(@RequestBody TokenDTO token){
        tokenRepository.deleteById(token.getTokenString());
    }
}
