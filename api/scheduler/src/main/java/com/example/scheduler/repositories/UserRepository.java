package com.example.scheduler.repositories;

import com.example.scheduler.entities.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Special Interface for a CrudRepository that works on user entities
 * Note: Implementations for this interface are automatically
 * generated by the Spring Framework. @Annotations should be used
 * for changing the implementation.
 */
public interface UserRepository extends CrudRepository<UsersEntity, Long> {

    @Query("SELECT user FROM UsersEntity user WHERE user.username = :username")
    Optional<UsersEntity> findUserByUsername(@Param("username")String username);

    @Query("SELECT user FROM UsersEntity user WHERE user.email = :email")
    Optional<UsersEntity> findUserByEmail(@Param("email")String email);

    UsersEntity findUserById(Long userIdP);
}
