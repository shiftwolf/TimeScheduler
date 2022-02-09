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
 * @author Max
 * @version 1.0
 * Entry-point for all user related REST-API requests
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    /**
     *  using constructor injection (Dependency injection)
     *  meaning this constructor is typically not used manually.
     * @param userRepository Repository that holds all registered Users (injected)
     *  @param tokenRepository Repository that holds all tokens of currently active Users (injected)
     */
    UserController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Lists all users saved in the database, converts them to DTOs and returns them
     * This request is intended to only be used by an admin since
     * normal users should not be granted access to all other users data
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return all Users registered in the database
     * @throws NoAuthorizationException if user authentication fails (wrong token or no admin status)
     */
    @GetMapping("/users")
    List<UserDTO> all( @RequestHeader("userId") Long userId,
                       @RequestHeader("token") String token)
            throws NoAuthorizationException{
        UsersEntity user = userRepository.findById(userId).orElseThrow(()-> new NoAuthorizationException(userId));
        if(tokenRepository.isValid(token, userId) && user.isAdmin()){
            List<UserDTO> dTOs = new ArrayList<>();
            for (UsersEntity e : userRepository.findAll()) {
                UserDTO dTO = new UserDTO(e.getId(),
                        e.getUsername(),
                        e.getName(),
                        e.getEmail(),
                        e.isAdmin());
                dTOs.add(dTO);
            }
            return dTOs;
        }
        else {
            throw new NoAuthorizationException(userId);
        }

    }

    /**
     * Converts User DTO to entity and saves it to the database
     * @param newUser DTO that hold all relevant data to create a new user
     * @return Http response if the registration was successful
     * @throws UsernameTakenException if username is already taken
     * @throws EmailAlreadyExistsException if email is already taken
     */
    @PostMapping("/users")
    ResponseEntity<String> newUser(@RequestBody NewUserDTO newUser)
                                throws UsernameTakenException,
                                EmailAlreadyExistsException {
        userRepository.findUserByUsername(newUser.getUsername()).ifPresent(arg -> {
            throw new UsernameTakenException(newUser.getUsername());
        });
        userRepository.findUserByEmail(newUser.getEmail()).ifPresent(arg -> {
            throw new EmailAlreadyExistsException(newUser.getEmail());
        });
        UsersEntity user = new UsersEntity(
                newUser.getEmail(),
                newUser.getUsername(),
                newUser.getName(),
                newUser.getPassword(),
                false
        ).hashPassword();
        return ResponseEntity.ok().body("User: " + userRepository.save(user).getId() +"created successfully");
    }

    /**
     * Edit Data of a specific user with the received data from the DTO
     * @param id user's ID in the database
     * @param user DTO that hold all relevant data to edit the user
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the edit was successful
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @PutMapping("/users/id={id}")
    ResponseEntity<String> editUser(@PathVariable Long id,
                                    @RequestBody UserDTO user,
                                    @RequestHeader("userId") Long userId,
                                    @RequestHeader("token") String token)
                                throws UserNotFoundException,
                                NoAuthorizationException {
        if(!validateUser(id,userId, token)){throw new NoAuthorizationException(userId);}
        UsersEntity entity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setName(user.getName());
        return ResponseEntity.ok().body("User: " + userRepository.save(entity).getId() +" edited successfully");
    }

    /**
     * Get Data of a specific user by including his id in the request url
     * @param id user's ID in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @GetMapping("/users/id={id}")
    UserDTO one(@PathVariable Long id,
                @RequestHeader("userId") Long userId,
                @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        if(!tokenRepository.isValid(token, userId)){throw new NoAuthorizationException(userId);}
        UsersEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.isAdmin());
    }

    /**
     * Get Data of a specific user by including his username in the request url
     * @param username user's name in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @GetMapping("/users/name={username}")
    UserDTO oneByName(@PathVariable String username,
                      @RequestHeader("userId") Long userId,
                      @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        if(!tokenRepository.isValid(token, userId)){throw new NoAuthorizationException(userId);}
        UsersEntity user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.isAdmin());
    }
    /**
     * Get Data of a specific user by including his email in the request url
     * @param email user's email in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @GetMapping("/users/email={email}")
    UserDTO oneByEmail(@PathVariable String email,
                       @RequestHeader("userId") Long userId,
                       @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        if(!tokenRepository.isValid(token, userId)){throw new NoAuthorizationException(userId);}
        UsersEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.isAdmin());
    }

    /**
     * Delete a specific user by including his id in the request url
     * @param id User ID
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the deletion was successful
     * @throws NoAuthorizationException if user authentication fails
     */
    @DeleteMapping("/users/id={id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id,
                                      @RequestHeader("userId") Long userId,
                                      @RequestHeader("token") String token)
            throws NoAuthorizationException{
        if(!validateUser(id, userId, token)){throw new NoAuthorizationException(userId);}
        userRepository.deleteById(id);
        return ResponseEntity.ok().body("User: " + id + "deleted successfully");
    }

    /**
     * Validates User to grant them access to user data, based on his admin role or if he is accessing his data
     * @param accessId of the user that the client wants to access
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     */
    boolean validateUser(Long accessId, Long userId, String token){
        if(!tokenRepository.isValid(token, userId)){ return false;}
        if(accessId.longValue() == userId.longValue()){ return true;}
        if(userRepository.findById(userId).isPresent()){
            UsersEntity admin = userRepository.findById(userId).get();
            return admin.isAdmin();
        }
        return false;
    }
}


