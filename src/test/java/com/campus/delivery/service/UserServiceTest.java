package com.campus.delivery.service;

import com.campus.delivery.entity.User;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.repository.UserRepository;
import com.campus.delivery.service.impl.UserServiceImpl;
import com.campus.delivery.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;
    private UserRegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        // 初始化测试数据
        registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("123456");
        registerDTO.setName("testUser");
    }

    @Test
    void testRegister_Success() {
        // 模拟：手机号/邮箱未被注册
        when(userRepository.existsByPhone(registerDTO.getPhone())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);

        // 模拟：保存用户返回结果
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setPhone(registerDTO.getPhone());
        savedUser.setEmail(registerDTO.getEmail());
        savedUser.setName(registerDTO.getName());
        savedUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        savedUser.setPointBalance(100);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // 执行测试
        User result = userService.register(registerDTO);

        // 断言
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getName());
        assertEquals(100, result.getPointBalance());
        assertTrue(passwordEncoder.matches("123456", result.getPassword()));

        // 验证 mock 方法被调用
        verify(userRepository, times(1)).existsByPhone(registerDTO.getPhone());
        verify(userRepository, times(1)).existsByEmail(registerDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_PhoneExists() {
        // 模拟：手机号已注册
        when(userRepository.existsByPhone(registerDTO.getPhone())).thenReturn(true);

        // 执行测试并断言异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registerDTO);
        });
        assertEquals("手机号已被注册", exception.getMessage());

        // 验证 save 方法未被调用
        verify(userRepository, never()).save(any(User.class));
    }
}