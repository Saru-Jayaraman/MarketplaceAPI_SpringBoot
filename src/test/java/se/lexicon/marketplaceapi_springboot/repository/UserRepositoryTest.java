package se.lexicon.marketplaceapi_springboot.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    User userDetails1;

    @BeforeEach
    public void setUp() {
        Profile profile1 = new Profile("Test1", "Test1", "female", "Sweden", LocalDate.of(1992, 1, 1));
        User user1 = new User("test1@gmail.com", "$@Ru1992", new ArrayList<>(), false, profile1);
        userDetails1 = userRepository.save(user1);
    }

    @Test
    public void testExistsByEmail() {
        Assertions.assertNotNull(userDetails1);
        Assertions.assertNotNull(userDetails1.getEmail());
        Assertions.assertTrue(userRepository.existsByEmail(userDetails1.getEmail()));
    }

    @Test
    public void testFindByEmail() {
        Assertions.assertNotNull(userDetails1);
        Assertions.assertNotNull(userDetails1.getEmail());
        userDetails1 = userRepository.findByEmail(userDetails1.getEmail());
        Assertions.assertNotNull(userDetails1);
    }
}
