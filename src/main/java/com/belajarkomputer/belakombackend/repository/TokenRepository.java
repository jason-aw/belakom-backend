package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.JwtToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<JwtToken, String> {
}
