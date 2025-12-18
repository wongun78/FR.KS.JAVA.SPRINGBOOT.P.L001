package fpt.kiennt169.springboot.controllers;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizDetailResponseDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizRequestDTO;
import fpt.kiennt169.springboot.dtos.quizzes.QuizResponseDTO;
import fpt.kiennt169.springboot.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponseDTO>> createQuiz(
            @Valid @RequestBody QuizRequestDTO requestDTO) {
        QuizResponseDTO response = quizService.createQuiz(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Quiz created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<QuizResponseDTO>>> getAllQuizzes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        PageResponseDTO<QuizResponseDTO> response = quizService.getAllQuizzes(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Quizzes retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponseDTO>> getQuizById(@PathVariable UUID id) {
        QuizResponseDTO response = quizService.getQuizById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Quiz retrieved successfully"));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<QuizDetailResponseDTO>> getQuizWithQuestions(@PathVariable UUID id) {
        QuizDetailResponseDTO response = quizService.getQuizWithQuestions(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Quiz with questions retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponseDTO>> updateQuiz(
            @PathVariable UUID id,
            @Valid @RequestBody QuizRequestDTO requestDTO) {
        QuizResponseDTO response = quizService.updateQuiz(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Quiz updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Quiz deleted successfully"));
    }

    @PostMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuizDetailResponseDTO>> addQuestionToQuiz(
            @PathVariable UUID quizId,
            @PathVariable UUID questionId) {
        QuizDetailResponseDTO response = quizService.addQuestionToQuiz(quizId, questionId);
        return ResponseEntity.ok(ApiResponse.success(response, "Question added to quiz successfully"));
    }

    @DeleteMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> removeQuestionFromQuiz(
            @PathVariable UUID quizId,
            @PathVariable UUID questionId) {
        quizService.removeQuestionFromQuiz(quizId, questionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Question removed from quiz successfully"));
    }
}
