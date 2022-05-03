package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.model.request.CreateQuizRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateQuizRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
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


@RestController
@RequestMapping(value = "/api/quiz")
@AllArgsConstructor
@Slf4j
public class QuizController {

  private final QuizService quizService;

  @GetMapping("/{quizId}")
  public ResponseEntity<?> getQuizById(@PathVariable String quizId) {
    try {
      Quiz result = quizService.getQuizById(quizId);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createQuiz(@RequestBody CreateQuizRequest request) {
    try {
      Quiz result = quizService.createQuiz(request);

      return ResponseEntity.status(HttpStatus.CREATED).body(
          new ApiResponse(true, "Quiz " + result.getQuizName() + " berhasil dibuat!"));

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateQuiz(@RequestBody UpdateQuizRequest request) {
    try {
      Quiz result = quizService.updateQuiz(request);

      return ResponseEntity.ok(
          new ApiResponse(true, "Quiz with id " + result.getId() + " successfully updated"));

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }


  @DeleteMapping("/delete/{quizId}")
  public ResponseEntity<?> deleteQuiz(@PathVariable String quizId) {
    try {
      quizService.deleteQuiz(quizId);
      return ResponseEntity.ok(new ApiResponse(true, "Quiz successfully deleted"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

}
