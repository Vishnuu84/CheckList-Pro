package com.yiu.backend.repository;

import com.yiu.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // 1. Arrange: Create and save a user
        User user = new User();
        user.setUsername("db_test_user");
        user.setPassword("password");
        userRepository.save(user);

        // 2. Act: Try to find that user
        Optional<User> found = userRepository.findByUsername("db_test_user");

        // 3. Assert: Verify the user exists and data is correct
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("db_test_user");
    }
}