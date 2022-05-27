package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;

import java.util.List;

public interface ProgressService {
  List<ChapterProgress> findChapterProgressesByTopicIdAndUserId(String topicId, String userId);
  List<TopicProgress> findTopicProgressesByUserId(String userId);
  TopicProgress updateOrCreateTopicProgress(String topicId, String userId);
  ChapterProgress updateOrCreateChapterProgress(ProgressVo progressVo);
}
