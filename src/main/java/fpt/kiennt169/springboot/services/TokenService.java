package fpt.kiennt169.springboot.services;

import java.util.Set;

import org.springframework.security.core.Authentication;

import fpt.kiennt169.springboot.entities.User;


public interface TokenService {
    String generateToken(User user, Set<String> roles);
    
    String generateRefreshToken(User user);
    
    boolean validateRefreshToken(String token);
    
    String getEmailFromRefreshToken(String token);

    Authentication getAuthenticationFromToken(String token);
}
