package se.lexicon.marketplaceapi_springboot.service;

import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;

import java.util.List;

public interface AuthenticationService {
    UserDTOView authenticateUser(UserDTOForm dto);

    List<AdvertisementDTOView> retrieveAll();

    List<AdvertisementDTOView> registerAdvertisement(AdvertisementDTOForm dto);

    List<AdvertisementDTOView> deRegisterAdvertisement(AdvertisementDTOForm dto);
}
