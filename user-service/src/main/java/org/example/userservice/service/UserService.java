package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void addUser(UserDto userDto) {
        var user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
        repository.save(user);
        log.info("User added: {}", user.getUsername());
    }

    public UserDto findByEmail(String email) {

        if (!repository.existsByEmail(email)) {
            return null;
        }

        var user = repository.findByEmail(email);

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public UserDto findByUsername(String username) {
        if (!repository.existsByUsername(username)) {
            return null;
        }

        var user = repository.findByUsername(username);

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public UserDto findById(Long id) {
        if (!repository.existsById(id)) {
            return null;
        }

        var user = repository.findById(id).get();

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
