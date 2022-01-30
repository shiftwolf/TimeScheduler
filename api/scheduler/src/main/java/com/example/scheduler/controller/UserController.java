package com.example.scheduler.controller;

import com.example.scheduler.DTOs.NewUserDTO;
import com.example.scheduler.DTOs.UserDTO;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.EmailAlreadyExistsException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.exceptions.UserNotFoundException;
import com.example.scheduler.exceptions.UsernameTakenException;
import com.example.scheduler.repositories.TokenRepository;
import com.example.scheduler.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry-point for all user related REST-API requests
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     * @param userRepository Repository that holds all registered Users
     *  @param tokenRepository Repository that holds all tokens of currently active Users
     *  using constructor injection (Dependency injection)
     *  meaning this constructor is typically not used manually.
     */
    UserController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    /**
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return all Users registered in the database
     * @throws NoAuthorizationException if user authentication fails
     * Lists all users saved in the database
     */
    @GetMapping("/users")
    List<UserDTO> all( @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
        List<UserDTO> dTOs = new ArrayList<>();
        for (UsersEntity e : userRepository.findAll()) {
            UserDTO dTO = new UserDTO(e.getId(), e.getUsername(), e.getName(), e.getEmail());
            dTOs.add(dTO);
        }
        return dTOs;
    }

    /**
     * @param newUser DTO that hold all relevant data to create a new user
     * @throws UsernameTakenException if username is already taken
     * @throws EmailAlreadyExistsException if email is already taken
     * Converts User DTO to entity and saves it to the database
     */
    @PostMapping("/users")
    ResponseEntity<String> newUser(@RequestBody NewUserDTO newUser) throws UsernameTakenException, EmailAlreadyExistsException {
        userRepository.findUserByUsername(newUser.getUsername()).ifPresent(arg -> {
            throw new UsernameTakenException(newUser.getUsername());
        });

        userRepository.findUserByEmail(newUser.getEmail()).ifPresent(arg -> {
            throw new EmailAlreadyExistsException(newUser.getEmail());
        });

        UsersEntity user = new UsersEntity(
                newUser.getEmail(), newUser.getUsername(), newUser.getName(), newUser.getPassword()
        ).hashPassword();

        return ResponseEntity.ok().body("User: " + userRepository.save(user).getId() +"created successfully");
    }

    /**
     * @param id user's ID in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     * Get Data of a specific user
     */
    @GetMapping("/users/{id}")
    UserDTO one(@PathVariable Long id, @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws UserNotFoundException, NoAuthorizationException {
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }

        UsersEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    /**
     * @param id User ID
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @throws NoAuthorizationException if user authentication fails
     * Delete a specific user
     */
    @DeleteMapping("/users/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id,  @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().body("User: " + id + "deleted successfully");
    }
}


