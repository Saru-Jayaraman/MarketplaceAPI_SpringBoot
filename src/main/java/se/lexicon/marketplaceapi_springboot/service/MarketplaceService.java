package se.lexicon.marketplaceapi_springboot.service;

import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;

import java.util.List;

public interface MarketplaceService {
    UserDTOView authenticateUser(UserDTOForm dto);

    UserDTOView registerAdvertisement(UserDTOForm userDTO);

    UserDTOView deRegisterAdvertisement(UserDTOForm userDTO);

    List<AdvertisementDTOView> retrieveByFilterOption(String optionType, String optionValue);

    List<AdvertisementDTOView> retrieveByMultipleFilterOption(String category, String city, String priceRange);

    List<AdvertisementDTOView> retrieveOrderedAdvertisements(String category, String city, String priceRange, String orderBy, String orderType);

    List<AdvertisementDTOView> retrieveAll();
}
