package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String hello() {
        return """
                
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
                
                """;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {
            userService.addUser(userDto);
            return ResponseEntity.ok("User added successfully "+userDto.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<?> findByEmail(@RequestParam String email) {
        try {
            UserDto userDto = userService.findByEmail(email);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<?> findByUsername(@RequestParam String username) {
        try {
            UserDto userDto = userService.findByUsername(username);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        try {
            UserDto userDto = userService.findById(id);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }
}
