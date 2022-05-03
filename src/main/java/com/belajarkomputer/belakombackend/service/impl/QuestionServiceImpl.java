package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Question;
import com.belajarkomputer.belakombackend.model.request.CreateQuestionRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateQuestionRequest;
import com.belajarkomputer.belakombackend.repository.QuestionRepository;
import com.belajarkomputer.belakombackend.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

  private QuestionRepository questionRepository;

  @Override
  public List<Question> getAllQuestionByQuizId(String quizId) {
    return this.questionRepository.findAllQuestionByQuizId(quizId);
  }

  @Override
  public Question getQuestionById(String quizId, String questionId) {
    return this.questionRepository.findQuestionByQuizIdAndQuestionId(quizId, questionId);
  }

  @Override
  public Question createQuestion(CreateQuestionRequest request) {

    if (questionRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Soal dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Question newQuestion = Question.builder()
        .quizId(request.getQuizId())
        .topicId(request.getTopicId())
        .type(request.getType())
        .question(request.getQuestion())
        .correctAnswer(request.getCorrectAnswer())
        .fakeAnswer(request.getFakeAnswer())
        .build();


    return this.questionRepository.save(newQuestion);
  }

  @Override
  public void deleteQuestion(String id) {
    if (!this.questionRepository.existsById(id)) {
      throw new BadRequestException("Soal dengan id " + " tidak ada!");
    }
    this.questionRepository.deleteById(id);
  }

  @Override
  public Question updateQuestion(UpdateQuestionRequest request) {
    if (questionRepository.existsByOrder(request.getOrder()) || request.getOrder() == 0) {
      throw new BadRequestException("Soal dengan no urut " + request.getOrder() + " tidak valid!");
    }

    Question newQuestion = Question.builder()
        .quizId(request.getQuizId())
        .topicId(request.getTopicId())
        .type(request.getType())
        .question(request.getQuestion())
        .correctAnswer(request.getCorrectAnswer())
        .fakeAnswer(request.getFakeAnswer())
        .build();

    return this.questionRepository.save(newQuestion);
  }


}
