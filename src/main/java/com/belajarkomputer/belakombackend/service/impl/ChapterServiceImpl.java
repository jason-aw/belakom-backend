package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import com.belajarkomputer.belakombackend.service.TopicService;
import com.belajarkomputer.belakombackend.service.FileStorageService;
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
  private FileStorageService fileStorageService;
  private TopicService topicService;

  @Override
  public List<Chapter> getAllChaptersByTopicId(String topicId) {

    Topic topic = topicService.findTopicById(topicId);

    List<Chapter> orderedChapters = new ArrayList<>();
    List<Chapter> chapters = this.chapterRepository.findAllChaptersByTopicId(topicId);

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
    if (!this.chapterRepository.findById(id).isPresent()) {
      throw new BadRequestException("Chapter with id " + id + "not found");
    }
    Chapter chapter = this.findChapterById(id);
    chapter.getImageAttachments().forEach(filename -> this.fileStorageService.delete(filename));
    topicService.removeChapterFromOrder(chapter.getTopicId(), id);

    this.chapterRepository.deleteById(id);
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
      throw new BadRequestException("Chapter with id " + id + "not found");
    }
    return chapter;
  }

}
