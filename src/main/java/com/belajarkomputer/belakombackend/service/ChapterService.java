package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;

import java.util.List;

public interface ChapterService {
  List<Chapter> getAllChaptersByTopicId(String topicId);
  Chapter createChapter(CreateChapterRequest request);
  void deleteChapter(String id);
  Chapter updateChapter(UpdateChapterRequest request);
}
