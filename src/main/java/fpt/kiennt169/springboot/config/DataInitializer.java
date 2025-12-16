package fpt.kiennt169.springboot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fpt.kiennt169.springboot.entities.Question;
import fpt.kiennt169.springboot.entities.Quiz;
import fpt.kiennt169.springboot.enums.QuestionTypeEnum;
import fpt.kiennt169.springboot.repositories.QuestionRepository;
import fpt.kiennt169.springboot.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Database initializer that runs on application startup.
 * Creates sample quizzes and questions if they don't exist.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            initQuizzes();
        };
    }

    /**
     * Initialize sample quizzes with questions
     */
    private void initQuizzes() {
        // Quiz 1: Java Programming
        if (!quizRepository.existsByTitle("Java Programming Basics")) {
            Quiz javaQuiz = new Quiz();
            javaQuiz.setTitle("Java Programming Basics");
            javaQuiz.setDescription("Test your knowledge of Java programming fundamentals");
            javaQuiz.setDurationMinutes(30);
            javaQuiz.setActive(true);
            javaQuiz = quizRepository.save(javaQuiz);
            log.info("Created quiz: {}", javaQuiz.getTitle());

            // Questions for Java Quiz
            createQuestion(javaQuiz, "What is the correct syntax to output 'Hello World' in Java?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
            createQuestion(javaQuiz, "Which of the following are primitive data types in Java?", 
                          QuestionTypeEnum.MULTIPLE_CHOICE, 10);
            createQuestion(javaQuiz, "What does JVM stand for?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
            createQuestion(javaQuiz, "Which keyword is used to inherit a class in Java?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
        }

        // Quiz 2: Spring Boot
        if (!quizRepository.existsByTitle("Spring Boot Fundamentals")) {
            Quiz springQuiz = new Quiz();
            springQuiz.setTitle("Spring Boot Fundamentals");
            springQuiz.setDescription("Assess your understanding of Spring Boot framework");
            springQuiz.setDurationMinutes(45);
            springQuiz.setActive(true);
            springQuiz = quizRepository.save(springQuiz);
            log.info("Created quiz: {}", springQuiz.getTitle());

            // Questions for Spring Boot Quiz
            createQuestion(springQuiz, "What annotation is used to create a REST controller in Spring Boot?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
            createQuestion(springQuiz, "Which of the following are Spring Boot Starter dependencies?", 
                          QuestionTypeEnum.MULTIPLE_CHOICE, 10);
            createQuestion(springQuiz, "What is the default port for Spring Boot application?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
        }

        // Quiz 3: Database
        if (!quizRepository.existsByTitle("Database Design")) {
            Quiz dbQuiz = new Quiz();
            dbQuiz.setTitle("Database Design");
            dbQuiz.setDescription("Test your database design and SQL knowledge");
            dbQuiz.setDurationMinutes(40);
            dbQuiz.setActive(true);
            dbQuiz = quizRepository.save(dbQuiz);
            log.info("Created quiz: {}", dbQuiz.getTitle());

            // Questions for Database Quiz
            createQuestion(dbQuiz, "What does SQL stand for?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
            createQuestion(dbQuiz, "Which SQL command is used to retrieve data from a database?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
            createQuestion(dbQuiz, "Which of the following are types of database relationships?", 
                          QuestionTypeEnum.MULTIPLE_CHOICE, 10);
        }

        // Quiz 4: Inactive Quiz (for testing)
        if (!quizRepository.existsByTitle("Advanced Algorithms")) {
            Quiz algoQuiz = new Quiz();
            algoQuiz.setTitle("Advanced Algorithms");
            algoQuiz.setDescription("Challenge yourself with complex algorithmic problems");
            algoQuiz.setDurationMinutes(60);
            algoQuiz.setActive(false);
            algoQuiz = quizRepository.save(algoQuiz);
            log.info("Created quiz: {} (inactive)", algoQuiz.getTitle());

            // Questions for Algorithm Quiz
            createQuestion(algoQuiz, "What is the time complexity of binary search?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 10);
            createQuestion(algoQuiz, "Which data structure uses LIFO principle?", 
                          QuestionTypeEnum.SINGLE_CHOICE, 5);
        }

        log.info("Database initialization completed!");
    }

    /**
     * Helper method to create and save a question
     */
    private void createQuestion(Quiz quiz, String content, QuestionTypeEnum type, int score) {
        Question question = new Question();
        question.setContent(content);
        question.setType(type);
        question.setScore(score);
        question.setQuiz(quiz);
        questionRepository.save(question);
        log.info("  - Created question: {} (Score: {})", content, score);
    }
}
