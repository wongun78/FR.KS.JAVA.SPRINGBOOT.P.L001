package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.QuestionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    
    QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO);
    
    List<QuestionResponseDTO> getAllQuestions();
    
    QuestionResponseDTO getQuestionById(UUID id);
    
    QuestionResponseDTO updateQuestion(UUID id, QuestionRequestDTO requestDTO);
    
    void deleteQuestion(UUID id);
}