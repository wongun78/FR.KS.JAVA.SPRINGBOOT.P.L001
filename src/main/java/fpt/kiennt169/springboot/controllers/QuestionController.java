package fpt.kiennt169.springboot.controllers;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.services.QuestionService;
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
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> createQuestion(
            @Valid @RequestBody QuestionRequestDTO requestDTO) {
        QuestionResponseDTO response = questionService.createQuestion(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Question created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<QuestionResponseDTO>>> getAllQuestions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        PageResponseDTO<QuestionResponseDTO> response = questionService.getAllQuestions(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Questions retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getQuestionById(@PathVariable UUID id) {
        QuestionResponseDTO response = questionService.getQuestionById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Question retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> updateQuestion(
            @PathVariable UUID id,
            @Valid @RequestBody QuestionRequestDTO requestDTO) {
        QuestionResponseDTO response = questionService.updateQuestion(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Question updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Question deleted successfully"));
    }
}
