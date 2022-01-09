package com.example.scheduler.repositories;

import com.example.scheduler.entities.UsersEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UsersEntity, Long> {
}
