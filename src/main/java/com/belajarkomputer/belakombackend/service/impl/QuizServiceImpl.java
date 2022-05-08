package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateQuizRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateQuizRequest;
import com.belajarkomputer.belakombackend.repository.QuizRepository;
import com.belajarkomputer.belakombackend.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

  private QuizRepository quizRepository;

  @Override
  public List<Quiz> getAllQuizByTopicId(String topicId){
    return this.quizRepository.findAllQuizByTopicId(topicId);
  }

  @Override
  public Quiz getQuizById(String quizId){
    Quiz quiz = this.quizRepository.findById(quizId).orElse(null);
    if (Objects.isNull(quiz)) {
      throw new BadRequestException("Quiz with id " + quizId + " does not exist");
    }
    return quiz;
  }

  @Override
  public Quiz createQuiz(CreateQuizRequest request){
    if (quizRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Quiz dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Quiz newQuiz = Quiz.builder()
        .quizName(request.getQuizName())
        .order(request.getOrder())
        .topicId(request.getTopicId())
        .build();

    return this.quizRepository.save(newQuiz);
  }

  @Override
  public void deleteQuiz(String id){
    if (!this.quizRepository.existsById(id)) {
      throw new BadRequestException("Quiz dengan id " + " tidak ada!");
    }
    this.quizRepository.deleteById(id);
  }

  @Override
  public Quiz updateQuiz(UpdateQuizRequest request) {
    if (quizRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Quiz dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Quiz newQuiz = Quiz.builder()
        .id(request.getId())
        .quizName(request.getQuizName())
        .order(request.getOrder())
        .topicId(request.getTopicId())
        .build();

    return this.quizRepository.save(newQuiz);
  }

}
