package fpt.kiennt169.springboot.dtos.submissions;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record QuizSubmissionRequestDTO(
    
    @NotNull(message = "{validation.submission.score.notnull}")
    @Min(value = 0, message = "{validation.submission.score.min}")
    Double score,
    
    @NotNull(message = "{validation.submission.userid.notnull}")
    UUID userId,
    
    @NotNull(message = "{validation.submission.quizid.notnull}")
    UUID quizId
) {}
