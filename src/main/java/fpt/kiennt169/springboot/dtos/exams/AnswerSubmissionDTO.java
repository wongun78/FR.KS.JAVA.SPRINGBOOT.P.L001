package fpt.kiennt169.springboot.dtos.exams;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * DTO for answer submission
 */
public record AnswerSubmissionDTO(
    @NotNull(message = "Question ID is required")
    UUID questionId,
    
    @NotEmpty(message = "At least one answer ID is required")
    List<UUID> answerIds
) {}
