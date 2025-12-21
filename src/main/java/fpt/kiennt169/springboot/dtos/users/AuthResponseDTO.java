package fpt.kiennt169.springboot.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    
    private String refreshToken;
    
    @Builder.Default
    private String type = "Bearer";
    
    private UserResponseDTO user;
    private Set<String> roles;
}
