package fpt.kiennt169.springboot.dtos.roles;

import fpt.kiennt169.springboot.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;

public record RoleRequestDTO(
    
    @NotNull(message = "Role name is required")
    RoleEnum name
) {}
