package fpt.kiennt169.springboot.dtos;

import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDTO {
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotNull(message = "Type is required")
    private QuestionTypeEnum type;
    
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be at least 1")
    private Integer score;
    
    @NotNull(message = "Quiz ID is required")
    private UUID quizId;
}
