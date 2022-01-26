package com.example.scheduler.repositories;

import com.example.scheduler.entities.TokensEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Special Interface for a CrudRepository that works on token entities
 * Note: Implementations for this interface are automatically
 * generated by the Spring Framework. @Annotations should be used
 * for changing the implementation.
 */
public interface TokenRepository extends CrudRepository<TokensEntity, String> {


}
