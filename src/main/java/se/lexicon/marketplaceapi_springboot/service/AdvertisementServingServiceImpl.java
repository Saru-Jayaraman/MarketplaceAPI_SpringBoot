package se.lexicon.marketplaceapi_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi_springboot.converter.AdvertisementConverterImpl;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.repository.AdvertisementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvertisementServingServiceImpl implements AdvertisementServingService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementConverterImpl advertisementConverter;

    @Autowired
    public AdvertisementServingServiceImpl(AdvertisementRepository advertisementRepository, AdvertisementConverterImpl advertisementConverter) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementConverter = advertisementConverter;
    }

    @Override
    public List<AdvertisementDTOView> retrieveByFilterOption(String optionType, String optionValue) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(optionType.equalsIgnoreCase("category")) {
            advertisementEntities = advertisementRepository.findByCategory(optionValue);
        } else if(optionType.equalsIgnoreCase("city")) {
            advertisementEntities = advertisementRepository.findByCity(optionValue);
        } else if(optionType.equalsIgnoreCase("price")) {
            String[] priceArray = optionValue.trim().split("-");
            advertisementEntities = advertisementRepository.findByPriceBetween(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }

    @Override
    public List<AdvertisementDTOView> retrieveByMultipleFilterOption(String category, String city, String priceRange) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(!category.isEmpty() && !city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetween(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty() && !city.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCategoryAndCity(category, city);
        } else if(!city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCityAndPriceBetween(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCategoryAndPriceBetween(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCategory(category);
        } else if(!city.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCity(city);
        } else if(!priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByPriceBetween(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }

    @Override
    public List<AdvertisementDTOView> retrieveOrderedAdvertisements(String category, String city, String priceRange, String orderBy, String orderType) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(!category.isEmpty() && !city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceAsc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceDesc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityAsc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityDesc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty() && !city.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByPriceAsc(category, city);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByPriceDesc(category, city);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByCityAsc(category, city);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByCityDesc(category, city);
            }
        } else if(!city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByPriceAsc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByPriceDesc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByCityAsc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByCityDesc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceAsc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceDesc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityAsc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityDesc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByPriceAsc(category);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByPriceDesc(category);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByCityAsc(category);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByCityDesc(category);
            }
        } else if(!city.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByPriceAsc(city);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByPriceDesc(city);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByCityAsc(city);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByCityDesc(city);
            }
        } else if(!priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByPriceAsc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByPriceDesc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByCityAsc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByCityDesc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }
}
