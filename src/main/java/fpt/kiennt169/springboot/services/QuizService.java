package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizDetailResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizRequestDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizResponseDTO;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface QuizService {
    
    QuizResponseDTO createQuiz(QuizRequestDTO requestDTO);
    
    PageResponseDTO<QuizResponseDTO> getAllQuizzes(Pageable pageable);
    
    QuizResponseDTO getQuizById(UUID id);
    
    QuizResponseDTO updateQuiz(UUID id, QuizRequestDTO requestDTO);
    
    void deleteQuiz(UUID id);

    QuizDetailResponseDTO getQuizWithQuestions(UUID id);
    
    QuizDetailResponseDTO addQuestionToQuiz(UUID quizId, UUID questionId);
    
    void removeQuestionFromQuiz(UUID quizId, UUID questionId);
}
