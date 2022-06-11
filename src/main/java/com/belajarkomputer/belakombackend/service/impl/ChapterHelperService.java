package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.repository.ChapterRepository;
import com.belajarkomputer.belakombackend.repository.TopicRepository;
import com.belajarkomputer.belakombackend.service.CommentService;
import com.belajarkomputer.belakombackend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ChapterHelperService {

  private final ChapterRepository chapterRepository;
  private final FileStorageService fileStorageService;
  private final TopicRepository topicRepository;
  private final CommentService commentService;

  public void deleteChaptersByTopicId(String topicId) {
    List<Chapter> chapters = this.chapterRepository.findChaptersByTopicId(topicId);
    for (Chapter c : chapters) {
      this.deleteChapter(c.getId());
    }
  }

  public void deleteChapter(String id) {
    Chapter chapter = this.chapterRepository.findById(id).orElse(null);
    if (Objects.isNull(chapter)) {
      throw new ResourceNotFoundException("Chapter", "id", id);
    }
    chapter.getImageAttachments().forEach(filename -> this.fileStorageService.delete(filename));
    this.removeChapterFromOrder(chapter.getTopicId(), id);
    this.commentService.deleteCommentByChapterId(id);

    this.chapterRepository.deleteById(id);
  }

  private void removeChapterFromOrder(String topicId, String chapterId) {
    Topic topic = this.topicRepository.findById(topicId).orElse(null);
    if (Objects.isNull(topic)) {
      throw new BadRequestException("Topic with id " + topicId + " does not exist");
    }
    topic.getChapterOrder().remove(chapterId);
    topicRepository.save(topic);
  }

}
