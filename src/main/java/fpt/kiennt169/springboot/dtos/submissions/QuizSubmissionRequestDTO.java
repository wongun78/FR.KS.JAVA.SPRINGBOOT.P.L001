package fpt.kiennt169.springboot.dtos.submissions;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record QuizSubmissionRequestDTO(
    
    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0")
    Double score,
    
    @NotNull(message = "User ID is required")
    UUID userId,
    
    @NotNull(message = "Quiz ID is required")
    UUID quizId
) {}
