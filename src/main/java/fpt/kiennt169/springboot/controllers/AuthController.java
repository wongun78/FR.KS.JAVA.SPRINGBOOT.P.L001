package fpt.kiennt169.springboot.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fpt.kiennt169.springboot.dtos.ApiResponse;
import fpt.kiennt169.springboot.dtos.users.AuthResponseDTO;
import fpt.kiennt169.springboot.dtos.users.LoginRequestDTO;
import fpt.kiennt169.springboot.dtos.users.RegisterRequestDTO;
import fpt.kiennt169.springboot.services.AuthService;
import fpt.kiennt169.springboot.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageUtil messageUtil;
    
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; 

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        
        AuthResponseDTO response = authService.login(loginRequest);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(response.getRefreshToken()).toString())
                .body(ApiResponse.success(response, messageUtil.getMessage("success.auth.login")));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO registerRequest) {
        log.info("Registration request received for email: {}", registerRequest.getEmail());
        
        AuthResponseDTO response = authService.register(registerRequest);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(response.getRefreshToken()).toString())
                .body(ApiResponse.created(response, messageUtil.getMessage("success.auth.register")));
    }
    
    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> refresh(
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
        log.info("Token refresh request received");
        
        AuthResponseDTO response = authService.refresh(refreshToken);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(response.getRefreshToken()).toString())
                .body(ApiResponse.success(response, messageUtil.getMessage("success.auth.refresh")));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("Logout request received");
        
        authService.logout();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie().toString())
                .body(ApiResponse.success(null, messageUtil.getMessage("success.auth.logout")));
    }
    
    /**
     * Create a secure HttpOnly cookie for refresh token
     */
    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
                .from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .sameSite("Strict")
                .build();
    }
    
    /**
     * Create a cookie to delete the refresh token
     */
    private ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie
                .from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
