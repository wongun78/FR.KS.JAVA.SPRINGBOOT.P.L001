package fpt.kiennt169.springboot.dtos.answers;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record AnswerRequestDTO(
    
    @NotBlank(message = "Content is required")
    String content,
    
    @NotNull(message = "IsCorrect flag is required")
    Boolean isCorrect,
    
    @NotNull(message = "Question ID is required")
    UUID questionId
) {}
