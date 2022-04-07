package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = User.COLLECTION_NAME)
public class User {
  public static final String COLLECTION_NAME = "users";
  @Id
  private String id;
  @Email
  @Indexed(unique = true)
  private String email;
  private String password;
  private String name;
  private Provider provider;
  private String providerId;
  private Role role;
  private String imageUrl;
}
