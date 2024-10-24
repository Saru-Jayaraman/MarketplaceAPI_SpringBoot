package se.lexicon.marketplaceapi_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUser_Email(String user_email);

    //Filtering
    List<Advertisement> findByCategory(String category);

    List<Advertisement> findByCity(String city);

    List<Advertisement> findByPriceBetween(Double price, Double price2);

    List<Advertisement> findByCategoryAndCity(String category, String city);

    List<Advertisement> findByCategoryAndPriceBetween(String category, Double price, Double price2);

    List<Advertisement> findByCityAndPriceBetween(String city, Double price, Double price2);

    List<Advertisement> findByCategoryAndCityAndPriceBetween(String category, String city, Double price, Double price2);

    //Sorting by Price
    List<Advertisement> findByCategoryOrderByPriceAsc(String category);

    List<Advertisement> findByCategoryOrderByPriceDesc(String category);

    List<Advertisement> findByCityOrderByPriceAsc(String city);

    List<Advertisement> findByCityOrderByPriceDesc(String city);

    List<Advertisement> findByPriceBetweenOrderByPriceAsc(Double price, Double price2);

    List<Advertisement> findByPriceBetweenOrderByPriceDesc(Double price, Double price2);

    List<Advertisement> findByCategoryAndCityOrderByPriceAsc(String category, String city);

    List<Advertisement> findByCategoryAndCityOrderByPriceDesc(String category, String city);

    List<Advertisement> findByCategoryAndPriceBetweenOrderByPriceAsc(String category, Double price, Double price2);

    List<Advertisement> findByCategoryAndPriceBetweenOrderByPriceDesc(String category, Double price, Double price2);

    List<Advertisement> findByCityAndPriceBetweenOrderByPriceAsc(String city, Double price, Double price2);

    List<Advertisement> findByCityAndPriceBetweenOrderByPriceDesc(String city, Double price, Double price2);

    List<Advertisement> findByCategoryAndCityAndPriceBetweenOrderByPriceAsc(String category, String city, Double price, Double price2);

    List<Advertisement> findByCategoryAndCityAndPriceBetweenOrderByPriceDesc(String category, String city, Double price, Double price2);

    //Sorting by City
    List<Advertisement> findByCategoryOrderByCityAsc(String category);

    List<Advertisement> findByCategoryOrderByCityDesc(String category);

    List<Advertisement> findByCityOrderByCityAsc(String city);

    List<Advertisement> findByCityOrderByCityDesc(String city);

    List<Advertisement> findByPriceBetweenOrderByCityAsc(Double price, Double price2);

    List<Advertisement> findByPriceBetweenOrderByCityDesc(Double price, Double price2);

    List<Advertisement> findByCategoryAndCityOrderByCityAsc(String category, String city);

    List<Advertisement> findByCategoryAndCityOrderByCityDesc(String category, String city);

    List<Advertisement> findByCategoryAndPriceBetweenOrderByCityAsc(String category, Double price, Double price2);

    List<Advertisement> findByCategoryAndPriceBetweenOrderByCityDesc(String category, Double price, Double price2);

    List<Advertisement> findByCityAndPriceBetweenOrderByCityAsc(String city, Double price, Double price2);

    List<Advertisement> findByCityAndPriceBetweenOrderByCityDesc(String city, Double price, Double price2);

    List<Advertisement> findByCategoryAndCityAndPriceBetweenOrderByCityAsc(String category, String city, Double price, Double price2);

    List<Advertisement> findByCategoryAndCityAndPriceBetweenOrderByCityDesc(String category, String city, Double price, Double price2);
}
