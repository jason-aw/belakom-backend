package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@AllArgsConstructor
@RedisHash("JwtToken")
public class JwtToken {
  @Id
  private String token;
  @TimeToLive
  private Long timeToLive;
}
