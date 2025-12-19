package fpt.kiennt169.springboot.dtos.answers;

import jakarta.validation.constraints.*;

public record AnswerRequestDTO(
    
    @NotBlank(message = "Content is required")
    String content,
    
    @NotNull(message = "IsCorrect flag is required")
    Boolean isCorrect
) {}
