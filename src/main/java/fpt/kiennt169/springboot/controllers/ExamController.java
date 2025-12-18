package fpt.kiennt169.springboot.controllers;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.exams.ExamSubmissionRequestDTO;
import fpt.kiennt169.springboot.dtos.exams.ExamSubmissionResultDTO;
import fpt.kiennt169.springboot.services.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    /**
     * POST /api/v1/exam/submit
     * Submit exam and get result
     * Logic: Tính điểm dựa trên số câu đúng. Lưu lịch sử vào bảng QuizSubmissions
     * Output: Kết quả thi (Score, Total Questions, Pass/Fail)
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ExamSubmissionResultDTO>> submitExam(
            @Valid @RequestBody ExamSubmissionRequestDTO requestDTO) {
        ExamSubmissionResultDTO result = examService.submitExam(requestDTO);
        
        String message = result.passed() 
                ? "Congratulations! You passed the exam with " + String.format("%.2f", result.percentage()) + "%" 
                : "Unfortunately, you did not pass. You scored " + String.format("%.2f", result.percentage()) + "%";
        
        return ResponseEntity.ok(ApiResponse.success(result, message));
    }
}
