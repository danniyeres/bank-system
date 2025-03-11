package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("tester")
                .email("test@example.com")
                .password("password")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("tester")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByEmail_UserExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDto foundUser = userService.findByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(userDto.getEmail(), foundUser.getEmail());
        assertEquals(userDto.getUsername(), foundUser.getUsername());
        assertEquals(userDto.getPassword(), foundUser.getPassword());
        assertEquals(userDto.getId(), foundUser.getId());

    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        UserDto foundUser = userService.findByEmail(user.getEmail());

        assertNull(foundUser);
    }

    @Test
    void testFindByUsername_UserExists() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        UserDto foundUser = userService.findByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(userDto.getUsername(), foundUser.getUsername());
        assertEquals(userDto.getEmail(), foundUser.getEmail());
        assertEquals(userDto.getPassword(), foundUser.getPassword());
        assertEquals(userDto.getId(), foundUser.getId());

    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        UserDto foundUser = userService.findByUsername(user.getUsername());

        assertNull(foundUser);
    }

    @Test
    void testFindById_UserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto foundUser = userService.findById(user.getId());

        assertNotNull(foundUser);
        assertEquals(userDto.getId(), foundUser.getId());
        assertEquals(userDto.getUsername(), foundUser.getUsername());
        assertEquals(userDto.getEmail(), foundUser.getEmail());
        assertEquals(userDto.getPassword(), foundUser.getPassword());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        UserDto foundUser = userService.findById(user.getId());

        assertNull(foundUser);
    }

}
