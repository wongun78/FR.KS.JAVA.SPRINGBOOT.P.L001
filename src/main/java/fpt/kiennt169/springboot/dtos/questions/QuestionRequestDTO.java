package fpt.kiennt169.springboot.dtos.questions;

import fpt.kiennt169.springboot.dtos.answers.AnswerRequestDTO;
import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record QuestionRequestDTO(
    
    @NotBlank(message = "{validation.question.content.notblank}")
    String content,
    
    @NotNull(message = "{validation.question.type.notnull}")
    QuestionTypeEnum type,
    
    @NotNull(message = "{validation.question.score.notnull}")
    @Min(value = 1, message = "{validation.question.score.min}")
    Integer score,
    
    @NotNull(message = "{validation.question.quizid.notnull}")
    UUID quizId,
    
    @NotEmpty(message = "{validation.question.answers.notempty}")
    @Valid
    List<AnswerRequestDTO> answers
) {}
