package fpt.kiennt169.springboot.dtos.users;

import jakarta.validation.constraints.*;
import java.util.Set;
import java.util.UUID;

public record UserRequestDTO(
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    
    Boolean active,
    
    Set<UUID> roleIds
) {}
