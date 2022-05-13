package com.belajarkomputer.belakombackend.security;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.UpdateUserRequest;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;
  private static final int LAST_SEEN_CHAPTERS_LIMIT = 10;

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

  public UserVo updateUserData(String userId, UpdateUserRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(
        () -> new ResourceNotFoundException("User", "id", userId)
    );
    if (!StringUtils.hasText(request.getCurrentlyLearningTopic()) &&
        !StringUtils.hasText(request.getLastSeenChapterId())) {
      throw new BadRequestException("Request is empty");
    }
    if (StringUtils.hasText(request.getCurrentlyLearningTopic())) {
      user.setCurrentlyLearningTopic(request.getCurrentlyLearningTopic());
    }
    if (StringUtils.hasText(request.getLastSeenChapterId())) {
      String chapterToAdd = request.getLastSeenChapterId();
      List<String> lastSeenChapters = user.getLastSeenChapters();
      int i = lastSeenChapters.indexOf(chapterToAdd);
      if (i != -1) {
        lastSeenChapters.remove(chapterToAdd);
      } else if (lastSeenChapters.size() >= LAST_SEEN_CHAPTERS_LIMIT) {
        lastSeenChapters.remove(lastSeenChapters.size() - 1);
      }
      lastSeenChapters.add(0, chapterToAdd);
    }

    User updatedUser = this.userRepository.save(user);
    return UserVo.builder()
        .name(updatedUser.getName())
        .imageUrl(updatedUser.getImageUrl())
        .lastSeenChapters(updatedUser.getLastSeenChapters())
        .currentlyLearningTopic(updatedUser.getCurrentlyLearningTopic())
        .build();
  }
}
