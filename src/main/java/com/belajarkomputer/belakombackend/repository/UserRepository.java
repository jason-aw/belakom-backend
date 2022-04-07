package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  User findByEmail(String email);
  boolean existsByEmail(String email);
}
