package fpt.kiennt169.springboot.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;

public interface QuestionService {
    
    QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO);
    
    PageResponseDTO<QuestionResponseDTO> getAllQuestions(Pageable pageable);
    
    QuestionResponseDTO getQuestionById(UUID id);
    
    QuestionResponseDTO updateQuestion(UUID id, QuestionRequestDTO requestDTO);
    
    void deleteQuestion(UUID id);
}