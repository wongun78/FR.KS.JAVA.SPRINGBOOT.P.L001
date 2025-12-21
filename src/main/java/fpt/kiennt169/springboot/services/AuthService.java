package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.users.AuthResponseDTO;
import fpt.kiennt169.springboot.dtos.users.LoginRequestDTO;
import fpt.kiennt169.springboot.dtos.users.RegisterRequestDTO;

public interface AuthService {
    
    AuthResponseDTO login(LoginRequestDTO loginRequest);
    
    AuthResponseDTO register(RegisterRequestDTO registerRequest);
    
    AuthResponseDTO refresh(String refreshToken);
    
    void logout();
}
