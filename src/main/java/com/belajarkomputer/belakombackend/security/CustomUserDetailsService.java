package com.belajarkomputer.belakombackend.security;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(email);

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User not found with email : " + email);
    }

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(String id) {
    User user = this.userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User", "id", id)
    );

    return UserPrincipal.create(user);
  }

  public UserVo findUserById(String id) {
    User user = this.userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User", "id", id)
    );

    return UserVo.builder()
        .name(user.getName())
        .imageUrl(user.getImageUrl())
        .lastSeenChapters(user.getLastSeenChapters())
        .currentlyLearningTopic(user.getCurrentlyLearningTopic())
        .build();
  }

  public UserVo updateCurrentlyLearningTopic(String userId, String topicId) {
    User user = this.userRepository.findById(userId).orElseThrow(
        () -> new ResourceNotFoundException("User", "id", userId)
    );
    user.setCurrentlyLearningTopic(topicId);
    User updatedUser = this.userRepository.save(user);
    return UserVo.builder()
        .name(updatedUser.getName())
        .imageUrl(updatedUser.getImageUrl())
        .lastSeenChapters(updatedUser.getLastSeenChapters())
        .currentlyLearningTopic(updatedUser.getCurrentlyLearningTopic())
        .build();
  }
}
