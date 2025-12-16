package fpt.kiennt169.springboot.dtos.questions;

import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record QuestionRequestDTO(
    
    @NotBlank(message = "Content is required")
    String content,
    
    @NotNull(message = "Type is required")
    QuestionTypeEnum type,
    
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be at least 1")
    Integer score,
    
    @NotNull(message = "Quiz ID is required")
    UUID quizId
) {}
