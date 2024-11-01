package se.lexicon.marketplaceapi_springboot.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileRepositoryTest {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;
    User userDetails1;

    @BeforeEach
    public void setUp() {
        Profile profile1 = new Profile("Test1", "Test1", "female", "Sweden", LocalDate.of(1992, 1, 1));
        User user1 = new User("test1@gmail.com", "$@Ru1992", new ArrayList<>(), false, profile1);
        userDetails1 = userRepository.save(user1);
    }

    @Test
    @Transactional
    public void testUpdateProfileById() {
        Assertions.assertNotNull(userDetails1.getProfile());
        Assertions.assertNotNull(userDetails1.getProfile().getProfileId());

        String expectedValue = "India";
        profileRepository.updateProfileById(userDetails1.getProfile().getProfileId(), expectedValue);
        entityManager.flush();
        entityManager.clear();

        Optional<Profile> optionalProfile = profileRepository.findById(userDetails1.getProfile().getProfileId());
        Assertions.assertTrue(optionalProfile.isPresent());

        Assertions.assertEquals(expectedValue, optionalProfile.get().getCountry());
    }
}
