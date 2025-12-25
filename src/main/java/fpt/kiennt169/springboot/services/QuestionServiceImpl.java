package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.entities.Answer;
import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.exceptions.ResourceNotFoundException;
import fpt.kiennt169.springboot.mappers.AnswerMapper;
import fpt.kiennt169.springboot.mappers.QuestionMapper;
import fpt.kiennt169.springboot.repositories.AnswerRepository;
import fpt.kiennt169.springboot.repositories.QuestionRepository;
import fpt.kiennt169.springboot.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final QuizRepository quizRepository; 
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    @Override
    public QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO) {
        // Validate all quiz IDs if provided
        if (requestDTO.quizIds() != null && !requestDTO.quizIds().isEmpty()) {
            for (UUID quizId : requestDTO.quizIds()) {
                if (!quizRepository.existsById(quizId)) {
                    throw new ResourceNotFoundException("Quiz", "id", quizId);
                }
            }
        }
        
        Question question = questionMapper.toEntity(requestDTO);
        Question savedQuestion = questionRepository.save(question);
        final UUID savedQuestionId = savedQuestion.getId();
        
        // Link question to quizzes
        if (requestDTO.quizIds() != null && !requestDTO.quizIds().isEmpty()) {
            for (UUID quizId : requestDTO.quizIds()) {
                fpt.kiennt169.springboot.entities.Quiz quiz = quizRepository.findById(quizId)
                        .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
                quiz.getQuestions().add(savedQuestion);
                quizRepository.save(quiz);
            }
        }
        
        final Question finalQuestion = savedQuestion;
        List<Answer> answers = requestDTO.answers().stream()
                .map(answerDTO -> {
                    Answer answer = answerMapper.toEntity(answerDTO);
                    answer.setQuestion(finalQuestion);
                    return answer;
                })
                .collect(Collectors.toList());
        
        answerRepository.saveAll(answers);
        savedQuestion.setAnswers(answers);
        
        // Reload to get updated quizzes
        Question reloadedQuestion = questionRepository.findById(savedQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", savedQuestionId));
        
        return questionMapper.toResponseDTO(reloadedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<QuestionResponseDTO> getAllQuestions(Pageable pageable) {
        Page<Question> questionPage = questionRepository.findAll(pageable);
        
        Page<QuestionResponseDTO> responsePage = questionPage.map(questionMapper::toResponseDTO);
        
        return PageResponseDTO.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDTO getQuestionById(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        return questionMapper.toResponseDTO(question);
    }

    @Override
    public QuestionResponseDTO updateQuestion(UUID id, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        // Validate all quiz IDs if provided
        if (requestDTO.quizIds() != null && !requestDTO.quizIds().isEmpty()) {
            for (UUID quizId : requestDTO.quizIds()) {
                if (!quizRepository.existsById(quizId)) {
                    throw new ResourceNotFoundException("Quiz", "id", quizId);
                }
            }
        }
        
        questionMapper.updateEntityFromDTO(requestDTO, question);
        
        // Update quiz relationships if provided
        if (requestDTO.quizIds() != null) {
            // Remove question from all current quizzes
            java.util.List<fpt.kiennt169.springboot.entities.Quiz> currentQuizzes = new java.util.ArrayList<>(question.getQuizzes());
            for (fpt.kiennt169.springboot.entities.Quiz quiz : currentQuizzes) {
                quiz.getQuestions().remove(question);
                quizRepository.save(quiz);
            }
            
            // Add question to new quizzes
            if (!requestDTO.quizIds().isEmpty()) {
                for (UUID quizId : requestDTO.quizIds()) {
                    fpt.kiennt169.springboot.entities.Quiz quiz = quizRepository.findById(quizId)
                            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
                    if (!quiz.getQuestions().contains(question)) {
                        quiz.getQuestions().add(question);
                        quizRepository.save(quiz);
                    }
                }
            }
        }
        
        answerRepository.deleteAll(question.getAnswers());
        
        final Question finalQuestion = question;
        List<Answer> newAnswers = requestDTO.answers().stream()
                .map(answerDTO -> {
                    Answer answer = answerMapper.toEntity(answerDTO);
                    answer.setQuestion(finalQuestion);
                    return answer;
                })
                .collect(Collectors.toList());
        
        answerRepository.saveAll(newAnswers);
        question.setAnswers(newAnswers);
        
        Question updatedQuestion = questionRepository.save(question);
        final UUID updatedQuestionId = updatedQuestion.getId();
        
        // Reload to get updated quizzes
        Question reloadedQuestion = questionRepository.findById(updatedQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", updatedQuestionId));
        
        return questionMapper.toResponseDTO(reloadedQuestion);
    }

    @Override
    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionRepository.deleteById(id);
    }
}
