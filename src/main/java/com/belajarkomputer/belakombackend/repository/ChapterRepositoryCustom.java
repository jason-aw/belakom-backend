package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Chapter;

import java.util.List;

public interface ChapterRepositoryCustom {
  List<Chapter> findAllChaptersByTopicId(String id);
}
