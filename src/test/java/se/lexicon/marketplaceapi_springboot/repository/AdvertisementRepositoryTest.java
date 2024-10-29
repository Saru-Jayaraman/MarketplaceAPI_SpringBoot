package se.lexicon.marketplaceapi_springboot.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdvertisementRepositoryTest {
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    UserRepository userRepository;
    Advertisement advertisementDetails1, advertisementDetails2, advertisementDetails3, advertisementDetails4, advertisementDetails5;
    User userDetails1, userDetails2;

    @BeforeEach
    public void setUp() {
        Profile profile1 = new Profile("Test1", "Test1", "female", "Sweden", LocalDate.of(1992, 1, 1));
        User user1 = new User("test1@gmail.com", "$@Ru1992", profile1);
        userDetails1 = userRepository.save(user1);
        advertisementDetails1 = advertisementRepository.save(new Advertisement("Lion toy", "Medium size soft toy", 200.0, "Toys", "Almhult", userDetails1));
        advertisementDetails2 = advertisementRepository.save(new Advertisement("Tiger toy", "Small size soft toy", 100.0, "Toys", "Almhult", userDetails1));
        advertisementDetails3 = advertisementRepository.save(new Advertisement("Hp Laptop", "2015 model", 10000.0, "Electronics", "Almhult", userDetails1));

        Profile profile2 = new Profile("Test2", "Test2", "male", "Sweden", LocalDate.of(1993, 1, 1));
        User user2 = new User("test2@gmail.com", "$@Ru1992", profile2);
        userDetails2 = userRepository.save(user2);
        advertisementDetails4 = advertisementRepository.save(new Advertisement("Monkey toy", "Large size soft toy", 280.0, "Toys", "Stockholm", userDetails2));
        advertisementDetails5 = advertisementRepository.save(new Advertisement("Dell Laptop", "2018 model", 11000.0, "Electronics", "Stockholm", userDetails2));

    }

    @Test
    public void testFindByUser_Email() {
        Assertions.assertNotNull(userDetails1);
        List<Advertisement> advertisements = advertisementRepository.findByUser_Email(userDetails1.getEmail());
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails3));
    }

    @Test
    public void testFindByCategory() {
        List<Advertisement> advertisements = advertisementRepository.findByCategory("Electronics");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails3, advertisementDetails5));
    }

    @Test
    public void testFindByCity() {
        List<Advertisement> advertisements = advertisementRepository.findByCity("Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails3));
    }

    @Test
    public void testFindByPriceBetween() {
        List<Advertisement> advertisements = advertisementRepository.findByPriceBetween(100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails4));
    }

    @Test
    public void testFindByCategoryAndCity() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCity("Toys", "Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndPriceBetween() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndPriceBetween("Toys", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails4));
    }

    @Test
    public void testFindByCityAndPriceBetween() {
        List<Advertisement> advertisements = advertisementRepository.findByCityAndPriceBetween("Almhult",100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityAndPriceBetween() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityAndPriceBetween("Toys", "Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryOrderByPriceAsc("Toys");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1, advertisementDetails4));
    }

    @Test
    public void testFindByCategoryOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryOrderByPriceDesc("Toys");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCityOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityOrderByPriceAsc("Stockholm");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails5));
    }

    @Test
    public void testFindByCityOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityOrderByPriceDesc("Stockholm");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails5, advertisementDetails4));
    }

    @Test
    public void testFindByPriceBetweenOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByPriceBetweenOrderByPriceAsc(100.0, 10000.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1, advertisementDetails4, advertisementDetails3));
    }

    @Test
    public void testFindByPriceBetweenOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByPriceBetweenOrderByPriceDesc(100.0, 10000.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails3, advertisementDetails4, advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityOrderByPriceAsc("Toys", "Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1));
    }

    @Test
    public void testFindByCategoryAndCityOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityOrderByPriceDesc("Toys", "Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndPriceBetweenOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceAsc("Toys", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1, advertisementDetails4));
    }

    @Test
    public void testFindByCategoryAndPriceBetweenOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceDesc("Toys", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCityAndPriceBetweenOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityAndPriceBetweenOrderByPriceAsc("Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1));
    }

    @Test
    public void testFindByCityAndPriceBetweenOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityAndPriceBetweenOrderByPriceDesc("Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityAndPriceBetweenOrderByPriceAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceAsc("Toys", "Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails2, advertisementDetails1));
    }

    @Test
    public void testFindByCategoryAndCityAndPriceBetweenOrderByPriceDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceDesc("Toys", "Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryOrderByCityAsc("Toys");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails4));
    }

    @Test
    public void testFindByCategoryOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryOrderByCityDesc("Toys");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCityOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityOrderByCityAsc("Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails3));
    }

    @Test
    public void testFindByCityOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityOrderByCityDesc("Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails3));
    }

    @Test
    public void testFindByPriceBetweenOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByPriceBetweenOrderByCityAsc(100.0, 12000.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails3, advertisementDetails4, advertisementDetails5));
    }

    @Test
    public void testFindByPriceBetweenOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByPriceBetweenOrderByCityDesc(100.0, 12000.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails5, advertisementDetails1, advertisementDetails2, advertisementDetails3));
    }

    @Test
    public void testFindByCategoryAndCityOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityOrderByCityAsc("Toys", "Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityOrderByCityDesc("Toys", "Almhult");
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndPriceBetweenOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityAsc("Toys", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2, advertisementDetails4));
    }

    @Test
    public void testFindByCategoryAndPriceBetweenOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityDesc("Toys", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails4, advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCityAndPriceBetweenOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityAndPriceBetweenOrderByCityAsc("Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCityAndPriceBetweenOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCityAndPriceBetweenOrderByCityDesc("Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityAndPriceBetweenOrderByCityAsc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityAsc("Toys", "Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }

    @Test
    public void testFindByCategoryAndCityAndPriceBetweenOrderByCityDesc() {
        List<Advertisement> advertisements = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityDesc("Toys", "Almhult", 100.0, 300.0);
        Assertions.assertNotNull(advertisements);
        Assertions.assertEquals(advertisements, List.of(advertisementDetails1, advertisementDetails2));
    }
}
