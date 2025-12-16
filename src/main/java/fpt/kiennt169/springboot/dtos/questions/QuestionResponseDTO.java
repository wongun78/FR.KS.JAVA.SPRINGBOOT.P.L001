package fpt.kiennt169.springboot.dtos.questions;

import fpt.kiennt169.springboot.enums.QuestionTypeEnum;

import java.util.UUID;

public record QuestionResponseDTO(
    UUID id,
    String content,
    QuestionTypeEnum type,
    Integer score,
    UUID quizId,
    String quizTitle
) {}
