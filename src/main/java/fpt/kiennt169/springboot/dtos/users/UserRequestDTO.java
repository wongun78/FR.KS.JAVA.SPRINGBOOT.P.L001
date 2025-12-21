package fpt.kiennt169.springboot.dtos.users;

import jakarta.validation.constraints.*;
import java.util.Set;
import java.util.UUID;

public record UserRequestDTO(
    
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.invalid}")
    String email,
    
    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 8, message = "{validation.password.size}")
    String password,
    
    @NotBlank(message = "{validation.fullname.notblank}")
    @Size(max = 100, message = "{validation.fullname.size}")
    String fullName,
    
    Boolean active,
    
    Set<UUID> roleIds
) {}
