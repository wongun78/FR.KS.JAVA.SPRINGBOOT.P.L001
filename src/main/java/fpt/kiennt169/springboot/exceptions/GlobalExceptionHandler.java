package fpt.kiennt169.springboot.exceptions;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.error("Application exception occurred: {}", ex.getMessage());

        String message = messageUtil.getMessage(
            ex.getMessageKey(), 
            ex.getMessageParams()
        );
        
        ApiResponse<Void> response = ApiResponse.error(
            ex.getHttpStatus().value(),
            message,
            Map.of("errorCode", ex.getErrorCode()),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
    
}
