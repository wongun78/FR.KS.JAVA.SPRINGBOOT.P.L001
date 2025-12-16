package fpt.kiennt169.springboot.dtos;

import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {
    
    private UUID id;
    private String content;
    private QuestionTypeEnum type;
    private Integer score;
    private UUID quizId;
    private String quizTitle;
}
