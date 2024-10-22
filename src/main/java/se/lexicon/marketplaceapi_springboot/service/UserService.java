package se.lexicon.marketplaceapi_springboot.service;

import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;

public interface UserService {
    UserDTOView authenticateUser(UserDTOForm dto);

    UserDTOView registerAdvertisement(UserDTOForm userDTO);
}
