package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.model.vo.ChapterVo;
import com.belajarkomputer.belakombackend.repository.ChapterProgressRepository;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import com.belajarkomputer.belakombackend.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {

  private ChapterRepository chapterRepository;
  private TopicService topicService;
  private ChapterProgressRepository chapterProgressRepository;
  private ChapterHelperService chapterHelperService;

  @Override
  public List<Chapter> getAllChaptersByTopicId(String topicId) {

    Topic topic = topicService.findTopicById(topicId);

    List<Chapter> orderedChapters = new ArrayList<>();
    List<Chapter> chapters = this.chapterRepository.findChaptersByTopicId(topicId);

    if (CollectionUtils.isEmpty(chapters)) {
      return chapters;
    }

    topic.getChapterOrder().forEach(orderedId -> {
      Optional<Chapter> chapterToAdd =
          chapters.stream().filter(chapter -> chapter.getId().equals(orderedId)).findFirst();
      chapterToAdd.ifPresent(orderedChapters::add);
    });
    
    return orderedChapters;
  }

  @Override
  public List<ChapterVo> getAllChaptersByTopicIdAndUserId(String topicId, String userId) {

    Topic topic = topicService.findTopicById(topicId);

    List<Chapter> orderedChapters = new ArrayList<>();
    List<Chapter> chapters = this.chapterRepository.findChaptersByTopicId(topicId);

    List<ChapterProgress> chapterProgressList =
        this.chapterProgressRepository.findChapterProgressesByTopicIdAndUserId(topicId, userId);

    if (CollectionUtils.isEmpty(chapters)) {
      return null;
    }

    topic.getChapterOrder().forEach(orderedId -> {
      Optional<Chapter> chapterToAdd =
          chapters.stream().filter(chapter -> chapter.getId().equals(orderedId)).findFirst();
      chapterToAdd.ifPresent(orderedChapters::add);
    });

    return createListOfChaptersResponse(orderedChapters, chapterProgressList);
  }

  private List<ChapterVo> createListOfChaptersResponse(List<Chapter> chapters, List<ChapterProgress> chapterProgressList) {
    List<ChapterVo> chapterVos = new ArrayList<>();
    chapters.forEach(chapter -> {
      ChapterProgress existingChapterProgress = chapterProgressList.stream()
          .filter(chapterProgress -> chapterProgress.getChapterId().equals(chapter.getId()))
          .findFirst().orElse(null);

      chapterVos.add(this.createChapterVo(chapter, existingChapterProgress));
    });

    return chapterVos;
  }

  private ChapterVo createChapterVo(Chapter chapter, ChapterProgress chapterProgress) {
    return ChapterVo.builder()
        .id(chapter.getId())
        .topicId(chapter.getTopicId())
        .enableQuiz(chapter.isEnableQuiz())
        .chapterName(chapter.getChapterName())
        .description(chapter.getDescription())
        .htmlContent(chapter.getHtmlContent())
        .imageAttachments(chapter.getImageAttachments())
        .questions(chapter.getQuestions())
        .chapterCompletion(
            Objects.isNull(chapterProgress) ? 0 :
                this.calculateChapterCompletion(chapterProgress, chapter.isEnableQuiz()))
        .build();
  }

  private double calculateChapterCompletion(ChapterProgress chapterProgress, boolean enableQuiz) {
    double completed = 0;

    if (enableQuiz) {
      completed += chapterProgress.isArticleCompleted() ? 0.5 : 0;
      completed += chapterProgress.isQuizCompleted() ? 0.25 : 0;
      completed += chapterProgress.getCorrect() == chapterProgress.getTotalQuestions() ? 0.25 : 0;
    } else {
      completed += chapterProgress.isArticleCompleted() ? 1 : 0;
    }

    return completed;
  }

  @Override
  public Chapter createChapter(CreateChapterRequest request) {

    Chapter newChapter = Chapter.builder()
        .chapterName(request.getChapterName())
        .topicId(request.getTopicId())
        .description(request.getDescription())
        .enableQuiz(request.isEnableQuiz())
        .htmlContent("")
        .build();

    Chapter savedChapter = this.chapterRepository.save(newChapter);
    topicService.addChapterList(request.getTopicId(), savedChapter.getId());

    return savedChapter;
  }

  @Override
  public void deleteChapter(String id) {
    chapterHelperService.deleteChapter(id);
  }

  @Override
  public Chapter updateChapter(UpdateChapterRequest request) {

    Chapter chapter = this.findChapterById(request.getId());
    chapter.setChapterName(request.getChapterName());
    chapter.setDescription(request.getDescription());
    chapter.setHtmlContent(request.getHtmlContent());
    chapter.setEnableQuiz(request.isEnableQuiz());
    chapter.setImageAttachments(request.getImageAttachments());
    chapter.setQuestions(request.getQuestions());
    return this.chapterRepository.save(chapter);
  }

  @Override
  public Chapter findChapterById(String id) {
    Chapter chapter = this.chapterRepository.findById(id).orElse(null);
    if (Objects.isNull(chapter)) {
      throw new ResourceNotFoundException("Chapter", "id", id);
    }
    return chapter;
  }

}
