package org.example.userservice.repository;

import org.example.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user1 = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .build();

        User user2 = User.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("password456")
                .build();

        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    void testFindAll() {
        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    void testFindByEmail_ExistingUser() {
        User user = userRepository.findByEmail("john@example.com");
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    void testExistsByEmail_ExistingUser() {
        boolean exists = userRepository.existsByEmail("john@example.com");
        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_NonExistingUser() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    void testFindByUsername_ExistingUser() {
        User user = userRepository.findByUsername("john_doe");
        assertNotNull(user);
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testExistsByUsername_ExistingUser() {
        boolean exists = userRepository.existsByUsername("john_doe");
        assertTrue(exists);
    }

    @Test
    void testExistsByUsername_NonExistingUser() {
        boolean exists = userRepository.existsByUsername("nonexistent");
        assertFalse(exists);
    }

    @Test
    void testFindById_ExistingUser() {
        // Получаем первого сохраненного пользователя (id будет сгенерирован)
        User savedUser = userRepository.findAll().get(0);
        Long id = savedUser.getId();
        var user = userRepository.findById(id);
        assertTrue(user.isPresent());
        assertEquals("john_doe", user.get().getUsername());
    }

    @Test
    void testFindById_NonExistingUser() {
        var user = userRepository.findById(100L);
        assertTrue(user.isEmpty());
    }

    @Test
    void testExistsById_ExistingUser() {
        User savedUser = userRepository.findAll().get(0);
        Long id = savedUser.getId();
        boolean exists = userRepository.existsById(id);
        assertTrue(exists);
    }

    @Test
    void testExistsById_NonExistingUser() {
        boolean exists = userRepository.existsById(100L);
        assertFalse(exists);
    }

    @Test
    void testSaveUser() {
        User user = User.builder()
                .username("tester")
                .email("tester@example.com")
                .password("password123")
                .build();

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("tester", userRepository.findByUsername("tester").getUsername());
    }

    @Test
    void testDeleteUser() {
        User savedUser = userRepository.findAll().get(0);
        Long id = savedUser.getId();
        userRepository.deleteById(id);
        var user = userRepository.findById(id);
        assertTrue(user.isEmpty());
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void testUpdateUser() {
        User savedUser = userRepository.findAll().get(0);
        Long id = savedUser.getId();
        var userOptional = userRepository.findById(id);
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        user.setUsername("new_username");
        user.setEmail("new_email@example.com");
        userRepository.save(user);

        User updatedUser = userRepository.findById(id).get();
        assertEquals("new_username", updatedUser.getUsername());
        assertEquals("new_email@example.com", updatedUser.getEmail());
        assertEquals("password123", updatedUser.getPassword());
    }
}