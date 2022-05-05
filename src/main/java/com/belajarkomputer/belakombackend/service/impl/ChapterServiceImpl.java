package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import com.belajarkomputer.belakombackend.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {

  private ChapterRepository chapterRepository;
  private FileStorageService fileStorageService;

  @Override
  public List<Chapter> getAllChaptersByTopicId(String topicId) {
    return this.chapterRepository.findAllChaptersByTopicId(topicId);
  }

  @Override
  public Chapter createChapter(CreateChapterRequest request) {

    if (chapterRepository.existsByOrder(request.getOrder())) {
      throw new BadRequestException("Chapter with duplicate order found");
    } else if (request.getOrder() <= 0) {
      throw new BadRequestException("Chapter cannot have order 0 or negative values");
    }

    Chapter newChapter = Chapter.builder()
        .chapterName(request.getChapterName())
        .order(request.getOrder())
        .topicId(request.getTopicId())
        .description(request.getDescription())
        .enableQuiz(request.isEnableQuiz())
        .htmlContent("")
        .build();

    return this.chapterRepository.save(newChapter);
  }

  @Override
  public void deleteChapter(String id) {
    if (!this.chapterRepository.findById(id).isPresent()) {
      throw new BadRequestException("Chapter with id " + id + "not found");
    }
    Chapter chapter = this.chapterRepository.findById(id).get();
    chapter.getImageAttachments().forEach(filename -> this.fileStorageService.delete(filename));
    this.chapterRepository.deleteById(id);
  }

  @Override
  public Chapter updateChapter(UpdateChapterRequest request) {
    if (chapterRepository.existsByOrder(request.getOrder())) {
      Chapter chapter = chapterRepository.findChapterByOrder(request.getOrder());
      if (!chapter.getId().equals(request.getId())) {
        throw new BadRequestException("Chapter with duplicate order found");
      }
    } else if (request.getOrder() <= 0) {
      throw new BadRequestException("Chapter cannot have order 0 or negative values");
    }

    Chapter chapter = this.findChapterById(request.getId());
    chapter.setChapterName(request.getChapterName());
    chapter.setDescription(request.getDescription());
    chapter.setOrder(request.getOrder());
    chapter.setHtmlContent(request.getHtmlContent());
    chapter.setEnableQuiz(request.isEnableQuiz());
    chapter.setImageAttachments(request.getImageAttachments());
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
