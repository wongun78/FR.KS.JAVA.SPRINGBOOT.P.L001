package fpt.kiennt169.springboot.controllers;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizDetailResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizRequestDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizResponseDTO;
import fpt.kiennt169.springboot.services.QuizService;
import fpt.kiennt169.springboot.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final MessageUtil messageUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponseDTO>> createQuiz(
            @Valid @RequestBody QuizRequestDTO requestDTO) {
        QuizResponseDTO response = quizService.createQuiz(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, messageUtil.getMessage("success.quiz.created")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<QuizResponseDTO>>> getAllQuizzes(Pageable pageable) {
        PageResponseDTO<QuizResponseDTO> response = quizService.getAllQuizzes(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.quiz.retrieved.all")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponseDTO>> getQuizById(@PathVariable UUID id) {
        QuizResponseDTO response = quizService.getQuizById(id);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.quiz.retrieved")));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<QuizDetailResponseDTO>> getQuizWithQuestions(@PathVariable UUID id) {
        QuizDetailResponseDTO response = quizService.getQuizWithQuestions(id);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.quiz.retrieved.with_questions")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponseDTO>> updateQuiz(
            @PathVariable UUID id,
            @Valid @RequestBody QuizRequestDTO requestDTO) {
        QuizResponseDTO response = quizService.updateQuiz(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.quiz.updated")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(ApiResponse.success(null, messageUtil.getMessage("success.quiz.deleted")));
    }

    @PostMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuizDetailResponseDTO>> addQuestionToQuiz(
            @PathVariable UUID quizId,
            @PathVariable UUID questionId) {
        QuizDetailResponseDTO response = quizService.addQuestionToQuiz(quizId, questionId);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.quiz.question_added")));
    }

    @DeleteMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> removeQuestionFromQuiz(
            @PathVariable UUID quizId,
            @PathVariable UUID questionId) {
        quizService.removeQuestionFromQuiz(quizId, questionId);
        return ResponseEntity.ok(ApiResponse.success(null, messageUtil.getMessage("success.quiz.question_removed")));
    }
}
