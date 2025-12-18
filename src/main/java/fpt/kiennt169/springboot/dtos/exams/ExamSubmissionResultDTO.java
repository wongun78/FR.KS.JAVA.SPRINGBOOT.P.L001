package fpt.kiennt169.springboot.dtos.exams;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for exam submission result
 */
public record ExamSubmissionResultDTO(
    UUID submissionId,
    UUID userId,
    UUID quizId,
    String quizTitle,
    Double score,
    Integer totalQuestions,
    Integer correctAnswers,
    Integer incorrectAnswers,
    Double percentage,
    Boolean passed,
    LocalDateTime submissionTime
) {}
