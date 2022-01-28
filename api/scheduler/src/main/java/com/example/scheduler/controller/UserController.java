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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Entry-point for all user related REST-API requests
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     * @param userRepository
     *  using constructor injection (Dependency injection)
     *  meaning this constructor is typically not used manually.
     */
    UserController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    /**
     * @return all Users registered in the database
     */
    @GetMapping("/users")
    List<UserDTO> all( @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
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
        ).hashPassword();

        userRepository.save(user);
    }

    /**
     * @param id user's ID in the database
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     */
    @GetMapping("/users/{id}")
    UserDTO one(@PathVariable Long id, @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws UserNotFoundException, NoAuthorizationException {
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
        if(userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException(id);
        }

        UsersEntity user = userRepository.findById(id).get();

        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    /**
     * @param id User ID
     * Delete a specific user
     */
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id,  @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
       userRepository.deleteById(id);
    }
}


