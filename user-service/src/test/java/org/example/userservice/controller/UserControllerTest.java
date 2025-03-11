package org.example.userservice.controller;

import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHello() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                
                http://localhost:8080/user/add (POST)
                // Add user
                {
                    "username": "username",
                    "email": "email",
                    "password": "password"
                }
                
                http://localhost:8080/user/getByEmail?email=email (GET)
                // Get user by email
                {
                    "id": 1,
                    "username": "username",
                    "email": "email",
                    "password": "password"
                }
                
                http://localhost:8080/user/getByUsername?username=username (GET)
                // Get user by username
                {
                    "id": 1,
                    "username": "username",
                    "email": "email",
                    "password": "password"
                }
                
                http://localhost:8080/user/getById?id=1 (GET)
                // Get user by id
                {
                    "id": 1,
                    "username": "username",
                    "email": "email",
                    "password": "password"
                }
                
                """));

    }

    @Test
    void testAddUser_Success() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        doNothing().when(userService).addUser(any(UserDto.class));

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User added successfully john_doe"));
    }

    @Test
    void testAddUser_FailureWithIllegalArgument() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(null)
                .username(null)
                .password(null)
                .email(null)
                .build();

        doThrow(new IllegalArgumentException("Invalid user data")).when(userService).addUser(any(UserDto.class));

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid user data"));
    }

    @Test
    void testAddUser_FailureWithException() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        doThrow(new RuntimeException("Server error")).when(userService).addUser(any(UserDto.class));

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error server: Server error"));
    }

    @Test
    void testFindByEmail_ExistingUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        when(userService.findByEmail("john@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/user/getByEmail")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));
    }

    @Test
    void testFindByEmail_NonExistingUser() throws Exception {
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(null);

        mockMvc.perform(get("/user/getByEmail")
                        .param("email", "nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyOrNullString()));
    }

    @Test
    void testFindByUsername_ExistingUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        when(userService.findByUsername("john_doe")).thenReturn(userDto);

        mockMvc.perform(get("/user/getByUsername")
                        .param("username", "john_doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));
    }

    @Test
    void testFindById_ExistingUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        when(userService.findById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/user/getById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));
    }
}