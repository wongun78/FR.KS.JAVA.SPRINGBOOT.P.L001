package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.exceptions.ResourceNotFoundException;
import fpt.kiennt169.springboot.mappers.QuestionMapper;
import fpt.kiennt169.springboot.repositories.QuestionRepository;
import fpt.kiennt169.springboot.repositories.QuizRepository;
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
    private final QuizRepository quizRepository; 
    private final QuestionMapper questionMapper;

    @Override
    public QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO) {
        if (!quizRepository.existsById(requestDTO.quizId())) {
            throw new ResourceNotFoundException("Quiz", "id", requestDTO.quizId());
        }
        
        Question question = questionMapper.toEntity(requestDTO);
        
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toResponseDTO(savedQuestion);
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
        
        if (requestDTO.quizId() != null && !quizRepository.existsById(requestDTO.quizId())) {
            throw new ResourceNotFoundException("Quiz", "id", requestDTO.quizId());
        }
        
        questionMapper.updateEntityFromDTO(requestDTO, question);
        
        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toResponseDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionRepository.deleteById(id);
    }
}
