package fpt.kiennt169.springboot.dtos.roles;

import fpt.kiennt169.springboot.enums.RoleEnum;
import java.util.UUID;

public record RoleResponseDTO(
    UUID id,
    RoleEnum name
) {}
