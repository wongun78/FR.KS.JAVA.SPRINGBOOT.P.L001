package fpt.kiennt169.springboot.dtos.questions;

import fpt.kiennt169.springboot.dtos.answers.AnswerResponseDTO;
import fpt.kiennt169.springboot.enums.QuestionTypeEnum;

import java.util.List;
import java.util.UUID;

public record QuestionResponseDTO(
    UUID id,
    String content,
    QuestionTypeEnum type,
    Integer score,
    List<QuizInfoDTO> quizzes,
    List<AnswerResponseDTO> answers
) {
    public record QuizInfoDTO(
        UUID id,
        String title
    ) {}
}
