package se.lexicon.marketplaceapi_springboot.service;

import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;

import java.util.List;

public interface AdvertisementServingService {
    List<AdvertisementDTOView> retrieveByFilterOption(String optionType, String optionValue);

    List<AdvertisementDTOView> retrieveByMultipleFilterOption(String category, String city, String priceRange);

    List<AdvertisementDTOView> retrieveOrderedAdvertisements(String category, String city, String priceRange, String orderBy, String orderType);
}
