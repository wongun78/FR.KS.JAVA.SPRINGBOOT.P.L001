package fpt.kiennt169.springboot.dtos.answers;

import java.util.UUID;

public record AnswerResponseDTO(
    UUID id,
    String content,
    Boolean isCorrect
) {}
