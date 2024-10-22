package se.lexicon.marketplaceapi_springboot.converter;

import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;

public interface UserConverter {
    User toUserEntity(UserDTOForm dto);

    UserDTOView toUserDTOView(User entity);
}
