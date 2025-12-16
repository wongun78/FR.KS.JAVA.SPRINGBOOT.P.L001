package fpt.kiennt169.springboot.entities;

import java.util.UUID;

public class Quiz {
    private UUID id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Boolean active;
}
//  Quizzes
// id (UUID): Primary Key.
// title: Not Null, Max 150 chars.
// description: Max 500 chars.
// duration_minutes: Not Null, Min 1.
// active: Boolean.
//  Questions
// id (UUID): Primary Key.
// content: Not Null, Not Blank.
// type: Enum (SINGLE_CHOICE, MULTIPLE_CHOICE).
// score: Integer, Min 1.
// Relationship: Many-to-One với Quiz.