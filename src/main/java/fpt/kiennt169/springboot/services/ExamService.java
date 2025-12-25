package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.submissions.ExamResultResponseDTO;
import fpt.kiennt169.springboot.dtos.submissions.ExamSubmissionRequestDTO;

public interface ExamService {
    
    ExamResultResponseDTO submitExam(ExamSubmissionRequestDTO requestDTO);
}
