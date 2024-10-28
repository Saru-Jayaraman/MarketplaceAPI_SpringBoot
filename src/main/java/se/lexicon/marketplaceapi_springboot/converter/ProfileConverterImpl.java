package se.lexicon.marketplaceapi_springboot.converter;

import org.springframework.stereotype.Component;
import se.lexicon.marketplaceapi_springboot.domain.dto.ProfileDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.ProfileDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public Profile formToEntitySave(ProfileDTOForm dto) {
        return Profile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .gender(dto.getGender())
                .country(dto.getCountry())
                .birthDate(dto.getBirthDate())
                .build();
    }

    @Override
    public ProfileDTOView entityToView(Profile entity) {
        return ProfileDTOView.builder()
                .profileId(entity.getProfileId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gender(entity.getGender())
                .country(entity.getCountry())
                .birthDate(entity.getBirthDate())
                .joinedDate(entity.getJoinedDate())
                .build();
    }

    @Override
    public Profile viewToEntity(ProfileDTOView dto) {
        return Profile.builder()
                .profileId(dto.getProfileId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .gender(dto.getGender())
                .country(dto.getCountry())
                .birthDate(dto.getBirthDate())
                .joinedDate(dto.getJoinedDate())
                .build();
    }
}
