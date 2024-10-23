package se.lexicon.marketplaceapi_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUser_Email(String user_email);

    List<Advertisement> findByCategory(String category);

    List<Advertisement> findByCity(String city);

    List<Advertisement> findByPriceBetween(Double price, Double price2);
}
