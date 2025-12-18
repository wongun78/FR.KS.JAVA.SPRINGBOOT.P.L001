package fpt.kiennt169.springboot.mappers;

import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.entities.Quiz;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
 
    @Mapping(source = "quiz.id", target = "quizId")
    @Mapping(source = "quiz.title", target = "quizTitle")
    QuestionResponseDTO toResponseDTO(Question question);
 
    @Mapping(target = "quiz", source = "quizId")  
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Question toEntity(QuestionRequestDTO requestDTO);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(QuestionRequestDTO requestDTO, @MappingTarget Question question);

    default Quiz mapIdToQuiz(UUID quizId) {
        if (quizId == null) {
            return null;
        }
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        return quiz;
    }
}
