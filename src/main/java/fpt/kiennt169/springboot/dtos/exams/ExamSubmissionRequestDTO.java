package fpt.kiennt169.springboot.dtos.exams;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * DTO for exam submission request
 */
public record ExamSubmissionRequestDTO(
    @NotNull(message = "User ID is required")
    UUID userId,
    
    @NotNull(message = "Quiz ID is required")
    UUID quizId,
    
    @NotEmpty(message = "Answers cannot be empty")
    @Valid
    List<AnswerSubmissionDTO> answers
) {}
