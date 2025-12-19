package fpt.kiennt169.springboot.services;

import fpt.kiennt169.springboot.dtos.PageResponseDTO;
import fpt.kiennt169.springboot.dtos.users.UserRequestDTO;
import fpt.kiennt169.springboot.dtos.users.UserResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    
    UserResponseDTO createUser(UserRequestDTO requestDTO);
    
    PageResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable);
    
    UserResponseDTO getUserById(UUID id);
    
    UserResponseDTO updateUser(UUID id, UserRequestDTO requestDTO);
    
    void deleteUser(UUID id);

    UserResponseDTO getUserByEmail(String email);

}
