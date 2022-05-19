package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Comment;
import com.belajarkomputer.belakombackend.model.entity.Role;
import com.belajarkomputer.belakombackend.model.request.CommentRequest;
import com.belajarkomputer.belakombackend.model.request.EditCommentRequest;
import com.belajarkomputer.belakombackend.model.vo.CommentsVo;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.repository.CommentRepository;
import com.belajarkomputer.belakombackend.security.CustomUserDetailsService;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepository;
  private CustomUserDetailsService userDetailsService;

  @Override
  public CommentsVo getCommentsByChapterId(String chapterId) throws ResourceNotFoundException {

    Set<String> userIdToGet = new HashSet<>();
    List<Comment> mainComments = this.commentRepository.findMainCommentsByChapterId(chapterId);
    if (CollectionUtils.isEmpty(mainComments)) {
      throw new ResourceNotFoundException("Comments", "chapterId", chapterId);
    }

    Map<String, List<Comment>> commentRepliesMap = new HashMap<>();
    for (Comment comment : mainComments) {
      String commentId = comment.getId();
      userIdToGet.add(comment.getUserId());
      List<Comment> replies = this.commentRepository.findCommentsByParentCommentId(commentId);
      commentRepliesMap.put(commentId, replies);
      userIdToGet.addAll(replies.stream().map(Comment::getUserId).collect(Collectors.toList()));
    }

    Map<String, UserVo> usersMap = new HashMap<>();
    for (String userId : userIdToGet) {
      UserVo userVo;
      try {
        userVo = userDetailsService.findUserDataById(userId);
      } catch (Exception e) {
        userVo = null;
      }
      usersMap.put(userId, userVo);
    }

    return CommentsVo.builder()
        .mainComments(mainComments)
        .commentRepliesMap(commentRepliesMap)
        .usersMap(usersMap)
        .build();
  }

  @Override
  public void createComment(String userId, CommentRequest request) {
    Comment comment = Comment.builder()
        .userId(userId)
        .chapterId(request.getChapterId())
        .content(request.getContent())
        .parentCommentId(request.getParentCommentId())
        .build();

    this.commentRepository.save(comment);
  }

  @Override
  public void editComment(UserPrincipal userPrincipal, EditCommentRequest request) throws ResourceNotFoundException {
    Comment comment = this.commentRepository.findById(request.getCommentId()).orElse(null);
    if (Objects.isNull(comment)) {
      throw new ResourceNotFoundException("Comment", "id", request.getCommentId());
    }

    if (Role.USER.equals(userPrincipal.getRole()) &&
        !comment.getUserId().equals(userPrincipal.getId())) {
      throw new BadRequestException("userId does not match");
    }

    comment.setContent(request.getContent());
    this.commentRepository.save(comment);
  }

  @Override
  public void deleteComment(UserPrincipal userPrincipal, String commentId) {
    Comment comment = this.commentRepository.findById(commentId).orElse(null);
    if (Objects.isNull(comment)) {
      throw new ResourceNotFoundException("Comment", "id", commentId);
    }
    if (Role.USER.equals(userPrincipal.getRole()) &&
        !comment.getUserId().equals(userPrincipal.getId())) {
      throw new BadRequestException("userId does not match");
    }
    this.commentRepository.deleteById(commentId);
  }
}
