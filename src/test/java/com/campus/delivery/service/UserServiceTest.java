package com.campus.delivery.service;

import com.campus.delivery.entity.User;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.repository.UserRepository;
import com.campus.delivery.service.impl.UserServiceImpl;
import com.campus.delivery.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testRegisterUser() {
        // 准备测试数据
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("password123");
        registerDTO.setName("张三");
        registerDTO.setStudentId("2020001");

        // 模拟数据库操作
        when(userRepository.existsByPhone(registerDTO.getPhone())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        User user = userService.register(registerDTO);

        // 验证结果
        assertNotNull(user);
        assertEquals(registerDTO.getPhone(), user.getPhone());
        assertEquals(registerDTO.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(registerDTO.getPassword(), user.getPassword()));
        assertEquals(registerDTO.getName(), user.getName());
        assertEquals(registerDTO.getStudentId(), user.getStudentId());
        assertEquals(100, user.getPointBalance());
        assertEquals(0, user.getFrozenPoints());
        assertEquals(1, user.getIdentityType());
        assertEquals(0, user.getAuditStatus());
        assertEquals(1, user.getCurrentRole());
        assertEquals(1, user.getStatus());

        // 验证方法调用
        verify(userRepository, times(1)).existsByPhone(registerDTO.getPhone());
        verify(userRepository, times(1)).existsByEmail(registerDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserPhoneExists() {
        // 准备测试数据
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");

        // 模拟数据库操作
        when(userRepository.existsByPhone(registerDTO.getPhone())).thenReturn(true);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registerDTO);
        });

        assertEquals("手机号已被注册", exception.getMessage());
    }

    @Test
    void testLogin() {
        // 准备测试数据
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setAccount("13800138000");
        loginDTO.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setPhone("13800138000");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setAuditStatus(1);
        user.setIdentityType(1);

        // 模拟数据库操作
        when(userRepository.findByPhoneOrEmail(loginDTO.getAccount(), loginDTO.getAccount())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getId(), user.getIdentityType())).thenReturn("test-jwt-token");

        // 执行测试
        String token = userService.login(loginDTO);

        // 验证结果
        assertNotNull(token);
        assertEquals("test-jwt-token", token);

        // 验证方法调用
        verify(userRepository, times(1)).findByPhoneOrEmail(loginDTO.getAccount(), loginDTO.getAccount());
        verify(jwtUtil, times(1)).generateToken(user.getId(), user.getIdentityType());
    }

    @Test
    void testLoginInvalidPassword() {
        // 准备测试数据
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setAccount("13800138000");
        loginDTO.setPassword("wrongpassword");

        User user = new User();
        user.setPhone("13800138000");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setAuditStatus(1);

        // 模拟数据库操作
        when(userRepository.findByPhoneOrEmail(loginDTO.getAccount(), loginDTO.getAccount())).thenReturn(Optional.of(user));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginDTO);
        });

        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void testSwitchRole() {
        // 准备测试数据
        Long userId = 1L;
        Integer newRole = 2; // 派送方

        User user = new User();
        user.setId(userId);
        user.setCurrentRole(1); // 原需求方

        // 模拟数据库操作
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        User updatedUser = userService.switchRole(userId, newRole);

        // 验证结果
        assertNotNull(updatedUser);
        assertEquals(newRole, updatedUser.getCurrentRole());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFreezePoints() {
        // 准备测试数据
        Long userId = 1L;
        Integer amount = 50;

        User user = new User();
        user.setId(userId);
        user.setPointBalance(100);
        user.setFrozenPoints(0);

        // 模拟数据库操作
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        User updatedUser = userService.freezePoints(userId, amount);

        // 验证结果
        assertNotNull(updatedUser);
        assertEquals(50, updatedUser.getPointBalance());
        assertEquals(50, updatedUser.getFrozenPoints());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFreezePointsInsufficient() {
        // 准备测试数据
        Long userId = 1L;
        Integer amount = 150;

        User user = new User();
        user.setId(userId);
        user.setPointBalance(100);
        user.setFrozenPoints(0);

        // 模拟数据库操作
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.freezePoints(userId, amount);
        });

        assertEquals("积分不足", exception.getMessage());
    }

    @Test
    void testUnfreezePoints() {
        // 准备测试数据
        Long userId = 1L;
        Integer amount = 50;

        User user = new User();
        user.setId(userId);
        user.setPointBalance(50);
        user.setFrozenPoints(50);

        // 模拟数据库操作
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        User updatedUser = userService.unfreezePoints(userId, amount);

        // 验证结果
        assertNotNull(updatedUser);
        assertEquals(100, updatedUser.getPointBalance());
        assertEquals(0, updatedUser.getFrozenPoints());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdatePoints() {
        // 准备测试数据
        Long userId = 1L;
        Integer amount = 30;

        User user = new User();
        user.setId(userId);
        user.setPointBalance(100);

        // 模拟数据库操作
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        User updatedUser = userService.updatePoints(userId, amount, 1);

        // 验证结果
        assertNotNull(updatedUser);
        assertEquals(130, updatedUser.getPointBalance());

        // 验证方法调用
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
