package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
  @Query(value = "{'chapterId': ?0, 'parentCommentId': {$exists: false}}", sort = "{'lastUpdated': 1}")
  List<Comment> findMainCommentsByChapterId(String chapterId);
  @Query(sort = "{'lastUpdated': 1}")
  List<Comment> findCommentsByParentCommentId(String parentCommentId);

  void deleteCommentsByChapterId(String chapterId);
  void deleteCommentByParentCommentId(String parentCommentId);
}
