package fpt.kiennt169.springboot.dtos.users;

import fpt.kiennt169.springboot.enums.RoleEnum;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String email,
    String fullName,
    Boolean active,
    Set<RoleEnum> roles,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
