package fpt.kiennt169.springboot.dtos.submissions;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuizSubmissionResponseDTO(
    UUID id,
    Double score,
    LocalDateTime submissionTime,
    UUID userId,
    String userEmail,
    String userFullName,
    UUID quizId,
    String quizTitle
) {}
