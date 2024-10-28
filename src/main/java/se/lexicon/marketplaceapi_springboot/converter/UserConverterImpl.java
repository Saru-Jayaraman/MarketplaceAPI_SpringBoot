package se.lexicon.marketplaceapi_springboot.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.ProfileDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;
import se.lexicon.marketplaceapi_springboot.util.CustomPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverterImpl implements UserConverter {

    AdvertisementConverterImpl advertisementConverter;
    ProfileConverterImpl profileConverter;
    CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    public UserConverterImpl(AdvertisementConverterImpl advertisementConverter, ProfileConverterImpl profileConverter, CustomPasswordEncoder customPasswordEncoder) {
        this.advertisementConverter = advertisementConverter;
        this.profileConverter = profileConverter;
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    public User formToEntity(UserDTOForm dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(customPasswordEncoder.encode(dto.getPassword()))
                .build();
    }

    @Override
    public UserDTOView entityToView(User entity) {
        ProfileDTOView profileDTOView = null;
        if(entity.getProfile() != null)
            profileDTOView = profileConverter.entityToView(entity.getProfile());
        List<Advertisement> advertisementEntities = entity.getAdvertisements();
        List<AdvertisementDTOView> advertisementDTOViews = new ArrayList<>();
        if (advertisementEntities != null) {
            advertisementDTOViews = advertisementEntities
                                        .stream()
                                        .map(advertisement -> advertisementConverter.entityToView(advertisement))
                                        .toList();
        }
        return UserDTOView.builder()
                .email(entity.getEmail())
                .profile(profileDTOView)
                .advertisements(advertisementDTOViews)
                .build();
    }

    @Override
    public User viewToEntity(UserDTOView dto) {
        Profile profileEntity = null;
        if(dto.getProfile() != null)
            profileEntity = profileConverter.viewToEntity(dto.getProfile());
        List<AdvertisementDTOView> advertisementDTOViews = dto.getAdvertisements();
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if (advertisementDTOViews != null) {
            advertisementEntities = advertisementDTOViews
                    .stream()
                    .map(advertisement -> advertisementConverter.viewToEntity(advertisement))
                    .toList();
        }
        return User.builder()
                .email(dto.getEmail())
                .profile(profileEntity)
                .advertisements(advertisementEntities)
                .build();
    }
}
