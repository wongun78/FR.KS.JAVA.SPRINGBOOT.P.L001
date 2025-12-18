package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.exams.AnswerSubmissionDTO;
import fpt.kiennt169.springboot.dtos.exams.ExamSubmissionRequestDTO;
import fpt.kiennt169.springboot.dtos.exams.ExamSubmissionResultDTO;
import fpt.kiennt169.springboot.entities.*;
import fpt.kiennt169.springboot.exceptions.ResourceNotFoundException;
import fpt.kiennt169.springboot.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {
    
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizSubmissionRepository submissionRepository;
    
    private static final double PASSING_PERCENTAGE = 70.0;

    /**
     * Submit exam and calculate score
     * Logic: Tính điểm dựa trên số câu đúng
     * Output: Kết quả thi (Score, Total Questions, Pass/Fail)
     */
    public ExamSubmissionResultDTO submitExam(ExamSubmissionRequestDTO requestDTO) {
        // Validate User exists
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", requestDTO.userId()));
        
        // Validate Quiz exists and is active
        Quiz quiz = quizRepository.findById(requestDTO.quizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", requestDTO.quizId()));
        
        if (!quiz.getActive()) {
            throw new IllegalStateException("Quiz is not active: " + quiz.getTitle());
        }
        
        // Get all questions in the quiz
        List<Question> quizQuestions = quiz.getQuestions();
        if (quizQuestions == null || quizQuestions.isEmpty()) {
            throw new IllegalStateException("Quiz has no questions");
        }
        
        // Calculate score
        int totalQuestions = quizQuestions.size();
        int correctAnswers = 0;
        double totalScore = 0.0;
        double maxScore = 0.0;
        
        // Create map of question submissions for easy lookup
        Map<UUID, AnswerSubmissionDTO> submissionMap = new HashMap<>();
        for (AnswerSubmissionDTO answerSubmission : requestDTO.answers()) {
            submissionMap.put(answerSubmission.questionId(), answerSubmission);
        }
        
        // Check each question
        for (Question question : quizQuestions) {
            maxScore += question.getScore();
            
            AnswerSubmissionDTO userAnswer = submissionMap.get(question.getId());
            if (userAnswer == null) {
                // Question not answered
                continue;
            }
            
            // Check if answer is correct
            if (isAnswerCorrect(question, userAnswer.answerIds())) {
                correctAnswers++;
                totalScore += question.getScore();
            }
        }
        
        int incorrectAnswers = totalQuestions - correctAnswers;
        double percentage = (totalScore / maxScore) * 100;
        boolean passed = percentage >= PASSING_PERCENTAGE;
        
        // Save submission to database
        QuizSubmission submission = new QuizSubmission();
        submission.setUser(user);
        submission.setQuiz(quiz);
        submission.setScore(totalScore);
        submission.setSubmissionTime(LocalDateTime.now());
        
        QuizSubmission savedSubmission = submissionRepository.save(submission);
        
        // Return result
        return new ExamSubmissionResultDTO(
                savedSubmission.getId(),
                user.getId(),
                quiz.getId(),
                quiz.getTitle(),
                totalScore,
                totalQuestions,
                correctAnswers,
                incorrectAnswers,
                percentage,
                passed,
                savedSubmission.getSubmissionTime()
        );
    }
    
    /**
     * Check if submitted answers are correct for a question
     */
    private boolean isAnswerCorrect(Question question, List<UUID> submittedAnswerIds) {
        List<Answer> questionAnswers = question.getAnswers();
        if (questionAnswers == null || questionAnswers.isEmpty()) {
            return false;
        }
        
        // Get all correct answer IDs
        Set<UUID> correctAnswerIds = new HashSet<>();
        for (Answer answer : questionAnswers) {
            if (answer.getIsCorrect()) {
                correctAnswerIds.add(answer.getId());
            }
        }
        
        // Compare submitted answers with correct answers
        Set<UUID> submittedSet = new HashSet<>(submittedAnswerIds);
        
        // Must match exactly (same size and same elements)
        return correctAnswerIds.equals(submittedSet);
    }
}
