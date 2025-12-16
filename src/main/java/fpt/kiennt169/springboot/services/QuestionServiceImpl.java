package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.entities.Quiz;
import fpt.kiennt169.springboot.exceptions.ResourceNotFoundException;
import fpt.kiennt169.springboot.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {
    
    private final QuestionRepository questionRepository;

    @Override
    public QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO) {
        // Quiz quiz = quizRepository.findById(requestDTO.quizId())
        // .orElseThrow(() -> new RuntimeException("Quiz not found"));
        // question.setQuiz(quiz);
        Quiz quiz = new Quiz();
        quiz.setId(requestDTO.quizId());
        
        Question question = new Question();
        question.setContent(requestDTO.content());
        question.setType(requestDTO.type());
        question.setScore(requestDTO.score());
        question.setQuiz(quiz);
        
        Question savedQuestion = questionRepository.save(question);
        return mapToResponseDTO(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<QuestionResponseDTO> getAllQuestions(Pageable pageable) {
        Page<Question> questionPage = questionRepository.findAll(pageable);
        Page<QuestionResponseDTO> responsePage = questionPage.map(this::mapToResponseDTO);
        return PageResponseDTO.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDTO getQuestionById(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        return mapToResponseDTO(question);
    }

    @Override
    public QuestionResponseDTO updateQuestion(UUID id, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        question.setContent(requestDTO.content());
        question.setType(requestDTO.type());
        question.setScore(requestDTO.score());
        
        if (!question.getQuiz().getId().equals(requestDTO.quizId())) {
            Quiz newQuiz = new Quiz();
            newQuiz.setId(requestDTO.quizId());
            question.setQuiz(newQuiz);
        }
        
        Question updatedQuestion = questionRepository.save(question);
        return mapToResponseDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionRepository.deleteById(id);
    }
    
    private QuestionResponseDTO mapToResponseDTO(Question question) {
        return new QuestionResponseDTO(
                question.getId(),
                question.getContent(),
                question.getType(),
                question.getScore(),
                question.getQuiz().getId(),
                question.getQuiz().getTitle()
        );
    }
}
