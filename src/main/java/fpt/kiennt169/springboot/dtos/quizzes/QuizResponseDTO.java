package fpt.kiennt169.springboot.dtos.quizzes;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuizResponseDTO(
    UUID id,
    String title,
    String description,
    Integer durationMinutes,
    Boolean active,
    Integer totalQuestions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
