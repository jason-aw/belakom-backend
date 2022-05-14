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

    double totalCompletion = this.calculateTopicCompletion(chapters, chapterProgressList);
    topicProgress.setTopicCompletion(totalCompletion);
    this.topicProgressRepository.save(topicProgress);
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
          .articleCompleted(progressVo.getArticleCompleted())
          .quizCompleted(false)
          .correct(progressVo.getCorrect())
          .totalQuestions(chapter.getQuestions().size())
          .build();
    } else {
      if (Objects.nonNull(progressVo.getArticleCompleted())) {
        //update article complete doang
        chapterProgress.setArticleCompleted(progressVo.getArticleCompleted());
      } else if (Objects.nonNull(progressVo.getQuizCompleted())) {
        //update quiz complete doang
        chapterProgress.setQuizCompleted(progressVo.getQuizCompleted());
        chapterProgress.setCorrect(progressVo.getCorrect());
      }
    }
    ChapterProgress result = this.chapterProgressRepository.save(chapterProgress);

    this.updateOrCreateTopicProgress(result.getTopicId(), progressVo.getUserId());

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

  private double calculateTopicCompletion(List<Chapter> chapterList,
      List<ChapterProgress> chapterProgressList) {
    int total = chapterList.size();
    total += (int) chapterList.stream().filter(Chapter::isEnableQuiz).count();
    double completed = 0;
    for (ChapterProgress chapterProgress : chapterProgressList) {
      completed += chapterProgress.isArticleCompleted() ? 1 : 0;
      if (chapterProgress.isQuizCompleted()) {
        completed += chapterProgress.isQuizCompleted() ? 0.5 : 0;
        completed += chapterProgress.getCorrect() == chapterProgress.getTotalQuestions() ? 0.5 : 0;
      }
    }

    return (double) completed / (double) total;
  }

}
