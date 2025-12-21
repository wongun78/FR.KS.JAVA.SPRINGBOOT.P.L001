package fpt.kiennt169.springboot.dtos.answers;

import jakarta.validation.constraints.*;

public record AnswerRequestDTO(
    
    @NotBlank(message = "{validation.answer.content.notblank}")
    String content,
    
    @NotNull(message = "{validation.answer.iscorrect.notnull}")
    Boolean isCorrect
) {}
