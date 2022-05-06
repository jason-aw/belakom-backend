package com.belajarkomputer.belakombackend.repository.impl;

import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.repository.ChapterRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ChapterRepositoryImpl implements ChapterRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Chapter> findAllChaptersByTopicId(String id) {
    Query query = new Query(Criteria.where("topicId").is(id));

    return mongoTemplate.find(query, Chapter.class, Chapter.COLLECTION_NAME);
  }

}
