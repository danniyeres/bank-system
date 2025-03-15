package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.UserDto;
import org.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public String hello() {
        return """
               
                http://localhost:8080/auth/register (POST)
                // Register user
                {
                    "username": "username",
                    "email": "email",
                    "password": "password"
                }
                
                http://localhost:8080/auth/login (POST)
                // Login user
                {
                    "username": "username",
                    "password": "password"
                }
                
                """;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto registerUser) {
        try {
            String result = authService.register(registerUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto loginUser) {
        try {
            String token = authService.login(loginUser);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error server: " + e.getMessage());
        }
    }
}
