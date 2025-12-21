package fpt.kiennt169.springboot.dtos.quizzes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QuizRequestDTO(
    
    @NotBlank(message = "{validation.quiz.title.notblank}")
    @Size(max = 150, message = "{validation.quiz.title.size}")
    String title,
    
    @Size(max = 500, message = "{validation.quiz.description.size}")
    String description,
    
    @NotNull(message = "{validation.quiz.duration.notnull}")
    @Min(value = 1, message = "{validation.quiz.duration.min}")
    Integer durationMinutes,
    
    Boolean active
) {}
