package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Question;
import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.model.request.CreateQuestionRequest;
import com.belajarkomputer.belakombackend.model.request.CreateQuizRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.service.QuestionService;
import com.belajarkomputer.belakombackend.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/question")
@AllArgsConstructor
@Slf4j
public class QuestionController {

  private final QuestionService questionService;

  @GetMapping("/{quizId}")
  public ResponseEntity<?> getAllQuestionsByQuizId(@PathVariable String quizId) {
    try {
      List<Question> result = questionService.getAllQuestionByQuizId(quizId);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @GetMapping("/{quizId}/{questionId}")
  public ResponseEntity<?> getQuestionByQuizIdAndQuestionId(@PathVariable String quizId,
      @PathVariable String questionId) {
    try {
      Question result = questionService.getQuestionById(quizId, questionId);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest request) {
    try {
      Question result = questionService.createQuestion(request);

      return ResponseEntity.status(HttpStatus.CREATED).body(
          new ApiResponse(true, "Pertanyaan " + result.getQuestion() + " berhasil dibuat!"));

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @DeleteMapping("/delete/{questionId}")
  public ResponseEntity<?> deleteQuestion(@PathVariable String questionId) {
    try {
      questionService.deleteQuestion(questionId);
      return ResponseEntity.ok(new ApiResponse(true, "Question successfully deleted"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }



}
