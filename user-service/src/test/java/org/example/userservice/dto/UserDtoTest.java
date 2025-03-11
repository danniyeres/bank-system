package org.example.userservice.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    @Test
    void testUserDtoBuilder() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .email("john@example.com")
                .build();

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("john_doe", userDto.getUsername());
        assertEquals("password123", userDto.getPassword());
        assertEquals("john@example.com", userDto.getEmail());
    }

    @Test
    void testUserDtoDefaultValues() {
        UserDto userDto = UserDto.builder().build();

        assertNull(userDto.getId());
        assertNull(userDto.getUsername());
        assertNull(userDto.getPassword());
        assertNull(userDto.getEmail());
    }
}
