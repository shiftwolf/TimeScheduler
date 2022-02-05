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
     * @param userRepository Repository that holds all registered Users (injected)
     *  @param tokenRepository Repository that holds all tokens of currently active Users (injected)
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
    List<UserDTO> all( @RequestHeader("userId") Long userId,
                       @RequestHeader("token") String token)
            throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
        List<UserDTO> dTOs = new ArrayList<>();
        for (UsersEntity e : userRepository.findAll()) {
            UserDTO dTO = new UserDTO(e.getId(),
                    e.getUsername(),
                    e.getName(),
                    e.getEmail());
            dTOs.add(dTO);
        }
        return dTOs;
    }

    /**
     * @param newUser DTO that hold all relevant data to create a new user
     * @return Http response if the registration was successful
     * @throws UsernameTakenException if username is already taken
     * @throws EmailAlreadyExistsException if email is already taken
     * Converts User DTO to entity and saves it to the database
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
     * @param id user's ID in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the edit was successful
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     * Edit Data of a specific user
     */
    @PutMapping("/users/{id}")
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
        return ResponseEntity.ok().body("User: " + userRepository.save(entity).getId() +"edited successfully");
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
    UserDTO one(@PathVariable Long id,
                @RequestHeader("userId") Long userId,
                @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        if(!validateUser(id, userId, token)){throw new NoAuthorizationException(userId);}
        UsersEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    /**
     * @param username user's name in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     * Get Data of a specific user
     */
    @GetMapping("/users/{username}")
    UserDTO oneByName(@PathVariable String username,
                      @RequestHeader("userId") Long userId,
                      @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        UsersEntity user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        if(!validateUser(user.getId(), userId, token)){throw new NoAuthorizationException(userId);}
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }
    /**
     * @param email user's email in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return DTO of the user with all information
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     * Get Data of a specific user
     */
    @GetMapping("/users/{email}")
    UserDTO oneByEmail(@PathVariable String email,
                       @RequestHeader("userId") Long userId,
                       @RequestHeader("token") String token)
            throws UserNotFoundException,
            NoAuthorizationException {
        UsersEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        if(!validateUser(user.getId(), userId, token)){throw new NoAuthorizationException(userId);}
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    /**
     * @param id User ID
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the deletion was successful
     * @throws NoAuthorizationException if user authentication fails
     * Delete a specific user
     */
    @DeleteMapping("/users/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id,
                                      @RequestHeader("userId") Long userId,
                                      @RequestHeader("token") String token)
            throws NoAuthorizationException{
        if(!validateUser(id, userId, token)){throw new NoAuthorizationException(userId);}
        userRepository.deleteById(id);
        return ResponseEntity.ok().body("User: " + id + "deleted successfully");
    }

    /**
     * @param accessId of the user that the client wants to access
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     *  validates User to grant them access to user data, based on his admin role or if he is accessing his data
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


