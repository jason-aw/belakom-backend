package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;

import java.util.List;

public interface ProgressService {
  List<TopicProgress> findTopicProgressByUserId(String userId) throws Exception;
  TopicProgress updateOrCreateTopicProgress(String topicId, String userId) throws Exception;
  void deleteTopicProgress(String id);

  void deleteChapterProgress(String id);
  ChapterProgress updateOrCreateChapterProgress(ProgressVo progressVo) throws Exception;
  List<ChapterProgress> findChapterProgressByTopicIdAndUserId(String topicId, String userId) throws Exception;
}
