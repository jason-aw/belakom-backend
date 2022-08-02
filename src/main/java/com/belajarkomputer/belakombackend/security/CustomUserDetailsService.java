package com.belajarkomputer.belakombackend.security;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.UpdateUserRequest;
import com.belajarkomputer.belakombackend.model.vo.UserChapterHistory;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.repository.TopicRepository;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;
  private final ChapterRepository chapterRepository;
  private final TopicRepository topicRepository;
  private static final int LAST_SEEN_CHAPTERS_LIMIT = 10;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(email);

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User dengan email " + email + " tidak ditemukan");
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
        .email(user.getEmail())
        .imageUrl(user.getImageUrl())
        .lastSeenChapters(user.getLastSeenChapters())
        .currentlyLearningTopic(user.getCurrentlyLearningTopic())
        .build();
  }

  public List<UserChapterHistory> getUserChapterData(UserVo userVo) {
    List<UserChapterHistory> chapterHistories = new ArrayList<>();
    for (String chapterId : userVo.getLastSeenChapters()) {
      Chapter chapter = this.chapterRepository.findById(chapterId).orElse(null);
      if (Objects.nonNull(chapter)) {
        UserChapterHistory userChapterHistory = new UserChapterHistory();
        userChapterHistory.setChapterId(chapterId);
        userChapterHistory.setChapterName(chapter.getChapterName());
        userChapterHistory.setTopicId(chapter.getTopicId());
        Topic topic = this.topicRepository.findById(chapter.getTopicId()).orElse(null);
        userChapterHistory.setTopicName(topic.getTopicName());
        chapterHistories.add(userChapterHistory);
      }
    }

    return chapterHistories;
  }

  public UserVo findUserDataById(String id) {
    User user = this.userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User", "id", id)
    );

    return UserVo.builder()
        .name(user.getName())
        .imageUrl(user.getImageUrl())
        .build();
  }

  public void updateUserData(String userId, UpdateUserRequest request) {
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

    this.userRepository.save(user);
  }
}
