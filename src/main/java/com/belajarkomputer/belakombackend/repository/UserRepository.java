package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  User findByEmail(String email);
  boolean existsByEmail(String email);
}
