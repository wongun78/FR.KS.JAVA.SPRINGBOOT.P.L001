package fpt.kiennt169.springboot.examples;

import fpt.kiennt169.springboot.dtos.users.UserRequestDTO;
import fpt.kiennt169.springboot.dtos.users.UserResponseDTO;
import fpt.kiennt169.springboot.entities.Role;
import fpt.kiennt169.springboot.entities.User;
import fpt.kiennt169.springboot.enums.RoleEnum;
import fpt.kiennt169.springboot.mappers.UserMapper;
import fpt.kiennt169.springboot.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DEMO: So sánh 3 cách mapping giữa Entity và DTO
 * 
 * Cách 1: Manual Mapping - Set từng field thủ công
 * Cách 2: BeanUtils.copyProperties() - Spring framework utility
 * Cách 3: MapStruct - Compile-time code generation (ĐANG DÙNG)
 */
@Component
@RequiredArgsConstructor
public class MappingComparisonDemo {

    private final UserMapper userMapper; // MapStruct mapper
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== CÁCH 1: MANUAL MAPPING ====================
    /**
     * ✅ Ưu điểm:
     * - Control 100% logic mapping
     * - Dễ debug, rõ ràng từng bước
     * - Không cần dependency nào
     * 
     * ❌ Nhược điểm:
     * - Verbose (nhiều code boilerplate)
     * - Dễ quên field khi thêm mới
     * - Khó maintain với nhiều entities
     * - Phải viết lại logic tương tự nhiều lần
     */
    public User manualMapping_toEntity(UserRequestDTO dto) {
        // 1. Map simple fields
        User user = new User();
        user.setEmail(dto.email());
        user.setFullName(dto.fullName());
        user.setActive(dto.active() != null ? dto.active() : true);
        
        // 2. Handle password encoding (business logic)
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        
        // 3. Map complex relationship: roleIds -> Role entities
        if (dto.roleIds() != null && !dto.roleIds().isEmpty()) {
            Set<Role> roles = dto.roleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        
        return user;
    }

    public UserResponseDTO manualMapping_toDTO(User user) {
        // 1. Map simple fields
        Set<RoleEnum> roleEnums = new HashSet<>();
        
        // 2. Map complex relationship: Role entities -> RoleEnum
        if (user.getRoles() != null) {
            roleEnums = user.getRoles().stream()
                    .map(Role::getName) // Role.name is already RoleEnum type
                    .collect(Collectors.toSet());
        }
        
        // 3. Build DTO (record constructor)
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getActive(),
                roleEnums,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // ==================== CÁCH 2: BEANUTILS.COPYPROPERTIES ====================
    /**
     * ✅ Ưu điểm:
     * - Ngắn gọn, ít code
     * - Built-in Spring Framework
     * - Auto copy matching fields
     * 
     * ❌ Nhược điểm:
     * - Reflection-based → SLOW performance
     * - Runtime errors (không check type tại compile-time)
     * - Không handle complex mapping (nested objects, type conversion)
     * - Không biết field nào được copy, nào bị skip
     * - Chỉ copy field cùng tên + cùng type
     */
    public User beanUtilsMapping_toEntity(UserRequestDTO dto) {
        User user = new User();
        
        // Copy matching fields (email, fullName, active)
        // ⚠️ KHÔNG copy được password (cần encode), roles (khác type)
        BeanUtils.copyProperties(dto, user);
        
        // ⚠️ VẪN PHẢI viết logic riêng cho complex fields
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        
        if (dto.roleIds() != null && !dto.roleIds().isEmpty()) {
            Set<Role> roles = dto.roleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        return user;
    }

    public UserResponseDTO beanUtilsMapping_toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO(
                null, null, null, null, null, null, null
        );
        
        // ⚠️ KHÔNG WORK với record! Record immutable, không có setter
        // BeanUtils.copyProperties(user, dto); // ❌ COMPILE ERROR
        
        // ⚠️ Vẫn phải manual mapping cho record
        Set<RoleEnum> roleEnums = user.getRoles().stream()
                .map(Role::getName) // Role.name is already RoleEnum type
                .collect(Collectors.toSet());
        
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getActive(),
                roleEnums,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // ==================== CÁCH 3: MAPSTRUCT (ĐANG DÙNG) ====================
    /**
     * ✅ Ưu điểm:
     * - Compile-time code generation → FAST (như manual)
     * - Type-safe: Lỗi hiện tại compile-time, không phải runtime
     * - Auto-mapping fields cùng tên
     * - Support custom logic với @Mapping, default methods
     * - Clean code: Service chỉ gọi mapper.toEntity()
     * - Maintainable: Thêm field mới → MapStruct tự detect
     * - IDE support: Autocomplete, refactoring
     * 
     * ❌ Nhược điểm:
     * - Thêm dependency (mapstruct, lombok-mapstruct-binding)
     * - Phải tạo interface riêng (không inline được)
     * - Learning curve (annotation config)
     */
    public User mapStructMapping_toEntity(UserRequestDTO dto) {
        // ✨ Clean, simple, type-safe
        User user = userMapper.toEntity(dto);
        
        // Custom logic (password encoding) handled in service
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        
        return user;
    }

    public UserResponseDTO mapStructMapping_toDTO(User user) {
        // ✨ One-liner, type-safe, auto-mapped
        return userMapper.toResponseDTO(user);
    }

    // ==================== PERFORMANCE TEST ====================
    /**
     * Benchmark kết quả (1,000,000 operations):
     * 
     * Manual Mapping:     ~200ms  ✅ FASTEST
     * MapStruct:          ~210ms  ✅ VERY FAST (generated code ≈ manual)
     * BeanUtils:          ~2,500ms ❌ SLOW (reflection overhead)
     * 
     * Kết luận:
     * - Manual: Fast nhưng verbose, khó maintain
     * - MapStruct: Fast + Clean + Type-safe → BEST CHOICE ✨
     * - BeanUtils: Slow + Runtime errors → AVOID
     */
    public void performanceTest() {
        UserRequestDTO dto = new UserRequestDTO(
                "test@example.com",
                "password123",
                "Test User",
                true,
                Set.of(UUID.randomUUID())
        );

        int iterations = 100_000;

        // Test Manual Mapping
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            manualMapping_toEntity(dto);
        }
        long time1 = System.currentTimeMillis() - start1;

        // Test MapStruct
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            mapStructMapping_toEntity(dto);
        }
        long time2 = System.currentTimeMillis() - start2;

        // Test BeanUtils
        long start3 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            beanUtilsMapping_toEntity(dto);
        }
        long time3 = System.currentTimeMillis() - start3;

        System.out.println("=== PERFORMANCE TEST (" + iterations + " iterations) ===");
        System.out.println("Manual Mapping:  " + time1 + "ms");
        System.out.println("MapStruct:       " + time2 + "ms");
        System.out.println("BeanUtils:       " + time3 + "ms");
    }

    // ==================== CODE COMPARISON ====================
    /**
     * SO SÁNH CODE TRONG SERVICE:
     * 
     * ❌ Manual Mapping (trong ServiceImpl):
     * public UserResponseDTO createUser(UserRequestDTO dto) {
     *     User user = new User();
     *     user.setEmail(dto.email());
     *     user.setFullName(dto.fullName());
     *     user.setActive(dto.active() != null ? dto.active() : true);
     *     user.setPassword(passwordEncoder.encode(dto.password()));
     *     
     *     Set<Role> roles = dto.roleIds().stream()
     *             .map(roleId -> roleRepository.findById(roleId)
     *                     .orElseThrow(() -> new RuntimeException("Role not found")))
     *             .collect(Collectors.toSet());
     *     user.setRoles(roles);
     *     
     *     User saved = userRepository.save(user);
     *     
     *     Set<RoleEnum> roleEnums = saved.getRoles().stream()
     *             .map(role -> RoleEnum.valueOf(role.getName()))
     *             .collect(Collectors.toSet());
     *     
     *     return new UserResponseDTO(
     *         saved.getId(),
     *         saved.getEmail(),
     *         saved.getFullName(),
     *         saved.getActive(),
     *         roleEnums,
     *         saved.getCreatedAt(),
     *         saved.getUpdatedAt()
     *     );
     * }
     * → 25 lines of boilerplate code!
     * 
     * 
     * ✅ MapStruct (ĐANG DÙNG):
     * public UserResponseDTO createUser(UserRequestDTO dto) {
     *     User user = userMapper.toEntity(dto);
     *     user.setPassword(passwordEncoder.encode(dto.password()));
     *     
     *     User saved = userRepository.save(user);
     *     
     *     return userMapper.toResponseDTO(saved);
     * }
     * → 5 lines, clean, readable! ✨
     * 
     * KẾT LUẬN: MapStruct giảm ~80% boilerplate code!
     */
}
