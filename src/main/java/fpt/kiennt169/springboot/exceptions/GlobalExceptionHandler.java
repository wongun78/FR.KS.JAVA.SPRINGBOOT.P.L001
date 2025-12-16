package fpt.kiennt169.springboot.exceptions;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all custom base exceptions.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        log.error("Application exception occurred: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(
            ex.getHttpStatus().value(),
            ex.getMessage(),
            Map.of("errorCode", ex.getErrorCode())
        );
        
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
    
}
