package org.example.authservice.service;

import org.example.authservice.dto.UserDto;
import org.example.authservice.feign.UserClient;
import org.example.authservice.kafka.KafkaMessageProducer;
import org.example.authservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserClient userClient;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KafkaMessageProducer kafkaMessageProducer;

    @InjectMocks
    private AuthService authService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password123");
        userDto.setEmail("testuser@example.com");
    }

    @Test
    public void testRegisterSuccess() {
        when(userClient.findByUsername("testuser")).thenReturn(null);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(jwtService.generateToken("testuser")).thenReturn("mocked-token");

        String token = authService.register(userDto);

        assertEquals("mocked-token", token);
        verify(userClient).addUser(any(User.class));
        verify(kafkaMessageProducer).sendMessage("testuser@example.com,auth,User registered: testuser");
    }

    @Test
    public void testRegisterUsernameTaken() {
        User existingUser = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .email("testuser@example.com")
                .build();
        when(userClient.findByUsername("testuser")).thenReturn(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(userDto);
        });
        assertEquals("Username is already taken", exception.getMessage());

        verify(userClient, never()).addUser(any(User.class));
        verify(kafkaMessageProducer, never()).sendMessage(anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    public void testLoginSuccess() {
        User user = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .email("testuser@example.com")
                .build();
        when(userClient.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("testuser@example.com")).thenReturn("mocked-token");

        String token = authService.login(userDto);

        assertEquals("mocked-token", token);
        verify(kafkaMessageProducer).sendMessage("testuser@example.com,auth,You logged in: testuser");
    }

    @Test
    public void testLoginUserNotFound() {
        when(userClient.findByUsername("testuser")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(userDto);
        });
        assertEquals("user not found", exception.getMessage());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(kafkaMessageProducer, never()).sendMessage(anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    public void testLoginInvalidPassword() {
        User user = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .email("testuser@example.com")
                .build();
        when(userClient.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(userDto);
        });
        assertEquals("Invalid username or password", exception.getMessage());

        verify(kafkaMessageProducer, never()).sendMessage(anyString());
        verify(jwtService, never()).generateToken(anyString());
    }
}