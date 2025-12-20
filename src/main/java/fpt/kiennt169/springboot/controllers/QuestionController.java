package fpt.kiennt169.springboot.controllers;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionRequestDTO;
import fpt.kiennt169.springboot.dtos.questions.QuestionResponseDTO;
import fpt.kiennt169.springboot.services.QuestionService;
import fpt.kiennt169.springboot.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final MessageUtil messageUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> createQuestion(
            @Valid @RequestBody QuestionRequestDTO requestDTO) {
        QuestionResponseDTO response = questionService.createQuestion(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, messageUtil.getMessage("success.question.created")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<QuestionResponseDTO>>> getAllQuestions(Pageable pageable) {
        PageResponseDTO<QuestionResponseDTO> response = questionService.getAllQuestions(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.question.retrieved.all")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getQuestionById(@PathVariable UUID id) {
        QuestionResponseDTO response = questionService.getQuestionById(id);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.question.retrieved")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> updateQuestion(
            @PathVariable UUID id,
            @Valid @RequestBody QuestionRequestDTO requestDTO) {
        QuestionResponseDTO response = questionService.updateQuestion(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtil.getMessage("success.question.updated")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success(null, messageUtil.getMessage("success.question.deleted")));
    }
}
