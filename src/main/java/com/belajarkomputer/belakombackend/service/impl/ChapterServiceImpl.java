package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {

  private ChapterRepository chapterRepository;

  @Override
  public List<Chapter> getAllChaptersByTopicId(String topicId) {
    return this.chapterRepository.findAllChaptersByTopicId(topicId);
  }

  @Override
  public Chapter createChapter(CreateChapterRequest request) {

    if (chapterRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Chapter dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Chapter newChapter = Chapter.builder()
        .chapterName(request.getChapterName())
        .order(request.getOrder())
        .topicId(request.getTopicId())
        .description(request.getDescription())
        .build();

    return this.chapterRepository.save(newChapter);
  }

  @Override
  public void deleteChapter(String id) {
    if (!this.chapterRepository.existsById(id)) {
      throw new BadRequestException("Chapter dengan id " + " tidak ada!");
    }
    this.chapterRepository.deleteById(id);
  }

  @Override
  public Chapter updateChapter(UpdateChapterRequest request) {
    if (chapterRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Chapter dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Chapter newChapter = Chapter.builder()
        .id(request.getId())
        .chapterName(request.getChapterName())
        .order(request.getOrder())
        .description(request.getDescription())
        .build();

    return this.chapterRepository.save(newChapter);
  }

}
