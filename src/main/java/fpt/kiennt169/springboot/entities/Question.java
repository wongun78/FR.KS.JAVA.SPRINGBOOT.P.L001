package fpt.kiennt169.springboot.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false) 
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionTypeEnum type;

    @Column(nullable = false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}