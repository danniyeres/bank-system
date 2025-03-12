package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.dto.UserDto;
import org.example.authservice.feign.UserClient;
import org.example.authservice.kafka.KafkaMessageProducer;
import org.example.authservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final KafkaMessageProducer kafkaMessageProducer;

    public String register(UserDto registerUser) {
        if (userClient.findByUsername(registerUser.getUsername()) != null)
            throw new IllegalArgumentException("Username is already taken");

        var user = User.builder()
                .username(registerUser.getUsername())
                .password(passwordEncoder.encode(registerUser.getPassword()))
                .email(registerUser.getEmail())
                .build();

        userClient.addUser(user);
        kafkaMessageProducer.sendMessage(user.getEmail()+",auth,User registered: " + user.getUsername());
        log.info("User registered: {}", user.getUsername());
        return jwtService.generateToken(user.getUsername());
    }

    public String login(UserDto loginUser) {
        var user = userClient.findByUsername(loginUser.getUsername());
        if (user == null)
            throw new IllegalArgumentException("user not found");

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid username or password");

        log.info("User logged in: {}", user.getUsername());
        kafkaMessageProducer.sendMessage(user.getEmail()+",auth,You logged in: " + user.getUsername());
        return jwtService.generateToken(user.getEmail());
    }
}
