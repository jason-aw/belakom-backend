package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;
import com.belajarkomputer.belakombackend.repository.ChapterProgressRepository;
import com.belajarkomputer.belakombackend.repository.TopicProgressRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import com.belajarkomputer.belakombackend.service.ProgressService;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class ProgressServiceImpl implements ProgressService {

  private TopicProgressRepository topicProgressRepository;
  private ChapterProgressRepository chapterProgressRepository;
  private ChapterService chapterService;

  @Override
  public List<TopicProgress> findTopicProgressByUserId(String userId)
      throws ResourceNotFoundException {
    List<TopicProgress> topicProgressList =
        this.topicProgressRepository.findTopicProgressesByUserId(userId);

    if (Collections.isEmpty(topicProgressList)) {
      throw new ResourceNotFoundException("Topic Progress", "userId", userId);
    }
    return topicProgressList;
  }

  @Override
  public TopicProgress updateOrCreateTopicProgress(String topicId, String userId) throws Exception {
    TopicProgress topicProgress =
        this.topicProgressRepository.findTopicProgressesByTopicIdAndUserId(topicId, userId)
            .orElse(null);

    if (Objects.isNull(topicProgress)) {
      topicProgress = TopicProgress.builder()
          .topicId(topicId)
          .userId(userId)
          .build();
    }

    List<Chapter> chapters = this.chapterService.getAllChaptersByTopicId(topicId);
    List<ChapterProgress> chapterProgressList =
        this.chapterProgressRepository.findChapterProgressesByTopicIdAndUserId(topicId, userId);

    double totalCompletion = this.calculateCompletion(chapters, chapterProgressList);
    topicProgress.setTopicCompletion(totalCompletion);
    return topicProgress;
  }

  @Override
  public void deleteTopicProgress(String id) {

  }

  @Override
  public void deleteChapterProgress(String id) {
    this.chapterProgressRepository.deleteById(id);
  }

  @Override
  public ChapterProgress updateOrCreateChapterProgress(ProgressVo progressVo) throws Exception {
    // update from FE
    ChapterProgress chapterProgress =
        this.chapterProgressRepository.findChapterProgressByChapterIdAndUserId(progressVo.getChapterId(),
            progressVo.getUserId()).orElse(null);

    if (Objects.isNull(chapterProgress)) {
      Chapter chapter = this.chapterService.findChapterById(progressVo.getChapterId());
      chapterProgress = ChapterProgress.builder()
          .userId(progressVo.getUserId())
          .chapterId(progressVo.getChapterId())
          .topicId(chapter.getTopicId())
          .articleCompleted(progressVo.isArticleCompleted())
          .quizCompleted(progressVo.isQuizCompleted())
          .correct(progressVo.getCorrect())
          .totalQuestions(chapter.getQuestions().size())
          .build();
    } else {
      chapterProgress.setArticleCompleted(progressVo.isArticleCompleted());
      chapterProgress.setQuizCompleted(progressVo.isQuizCompleted());
      chapterProgress.setCorrect(progressVo.getCorrect());
    }
    ChapterProgress result = this.chapterProgressRepository.save(chapterProgress);

    this.updateOrCreateTopicProgress(progressVo.getTopicId(), progressVo.getUserId());

    return result;
  }

  @Override
  public List<ChapterProgress> findChapterProgressByTopicIdAndUserId(String topicId,
      String userId) throws ResourceNotFoundException {
    List<ChapterProgress> chapterProgressList =
        this.chapterProgressRepository.findChapterProgressesByTopicIdAndUserId(topicId, userId);

    if (CollectionUtils.isEmpty(chapterProgressList)) {
      throw new ResourceNotFoundException(
          "Chapter Progress with [topicId, userId] not found for: " + topicId + ", " + userId);
    }
    return chapterProgressList;
  }

  private double calculateCompletion(List<Chapter> chapterList,
      List<ChapterProgress> chapterProgressList) {
    int total = chapterList.size();
    total += (int) chapterList.stream().filter(Chapter::isEnableQuiz).count();
    int completed = 0;
    for (ChapterProgress chapterProgress : chapterProgressList) {
      completed = chapterProgress.isArticleCompleted() ? 1 : 0;
      completed = chapterProgress.isQuizCompleted() ? 1 : 0;
    }

    return (double) completed / (double) total;
  }

}
