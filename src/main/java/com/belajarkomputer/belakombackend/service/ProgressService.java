package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;

public interface ProgressService {
  TopicProgress updateOrCreateTopicProgress(String topicId, String userId);
  ChapterProgress updateOrCreateChapterProgress(ProgressVo progressVo);
}
