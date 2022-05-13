package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.request.CommentRequest;
import com.belajarkomputer.belakombackend.model.request.EditCommentRequest;
import com.belajarkomputer.belakombackend.model.vo.CommentsVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;

public interface CommentService {
  CommentsVo getCommentsByChapterId(String chapterId) throws Exception;
  void createComment(String userId, CommentRequest request);
  void editComment(UserPrincipal userPrincipal, EditCommentRequest request) throws Exception;
  void deleteComment(UserPrincipal userPrincipal, String commentId);
}
