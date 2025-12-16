package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.QuestionResponseDTO;
import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.entities.Quiz;
import fpt.kiennt169.springboot.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {
    
    private final QuestionRepository questionRepository;

    @Override
    public QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO) {
        Quiz quiz = new Quiz();
        quiz.setId(requestDTO.getQuizId());
        
        Question question = new Question();
        question.setContent(requestDTO.getContent());
        question.setType(requestDTO.getType());
        question.setScore(requestDTO.getScore());
        question.setQuiz(quiz);
        
        Question savedQuestion = questionRepository.save(question);
        return mapToResponseDTO(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDTO getQuestionById(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return mapToResponseDTO(question);
    }

    @Override
    public QuestionResponseDTO updateQuestion(UUID id, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        question.setContent(requestDTO.getContent());
        question.setType(requestDTO.getType());
        question.setScore(requestDTO.getScore());
        
        if (!question.getQuiz().getId().equals(requestDTO.getQuizId())) {
            Quiz newQuiz = new Quiz();
            newQuiz.setId(requestDTO.getQuizId());
            question.setQuiz(newQuiz);
        }
        
        Question updatedQuestion = questionRepository.save(question);
        return mapToResponseDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
    
    private QuestionResponseDTO mapToResponseDTO(Question question) {
        return QuestionResponseDTO.builder()
                .id(question.getId())
                .content(question.getContent())
                .type(question.getType())
                .score(question.getScore())
                .quizId(question.getQuiz().getId())
                .quizTitle(question.getQuiz().getTitle())
                .build();
    }
}
