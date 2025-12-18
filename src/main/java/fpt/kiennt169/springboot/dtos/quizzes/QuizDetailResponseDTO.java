package fpt.kiennt169.springboot.dtos.quizzes;

import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record QuizDetailResponseDTO(
    UUID id,
    String title,
    String description,
    Integer durationMinutes,
    Boolean active,
    List<QuestionResponseDTO> questions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
