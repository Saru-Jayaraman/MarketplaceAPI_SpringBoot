package se.lexicon.marketplaceapi_springboot.converter;

import se.lexicon.marketplaceapi_springboot.domain.dto.ProfileDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.ProfileDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;

public interface ProfileConverter {
    Profile formToEntitySave(ProfileDTOForm dto);

    ProfileDTOView entityToView(Profile entity);

    Profile viewToEntity(ProfileDTOView dto);
}
