package se.lexicon.marketplaceapi_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.marketplaceapi_springboot.converter.AdvertisementConverterImpl;
import se.lexicon.marketplaceapi_springboot.converter.ProfileConverterImpl;
import se.lexicon.marketplaceapi_springboot.converter.UserConverterImpl;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;
import se.lexicon.marketplaceapi_springboot.exception.DataNotFoundException;
import se.lexicon.marketplaceapi_springboot.exception.WrongPasswordException;
import se.lexicon.marketplaceapi_springboot.repository.AdvertisementRepository;
import se.lexicon.marketplaceapi_springboot.repository.ProfileRepository;
import se.lexicon.marketplaceapi_springboot.repository.UserRepository;
import se.lexicon.marketplaceapi_springboot.util.CustomPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ProfileRepository profileRepository;
    private final UserConverterImpl userConverter;
    private final AdvertisementConverterImpl advertisementConverter;
    private final ProfileConverterImpl profileConverter;
    private final CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, AdvertisementRepository advertisementRepository, ProfileRepository profileRepository, UserConverterImpl userConverter, AdvertisementConverterImpl advertisementConverter, ProfileConverterImpl profileConverter, CustomPasswordEncoder customPasswordEncoder) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.profileRepository = profileRepository;
        this.userConverter = userConverter;
        this.advertisementConverter = advertisementConverter;
        this.profileConverter = profileConverter;
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    public UserDTOView authenticateUser(UserDTOForm dto) {
        if(dto == null)
            throw new IllegalArgumentException("User Form cannot be null...");

        // User not exist --> SignUp operation --> Create user in User database
        if(!userRepository.existsByEmail(dto.getEmail())) {
            User userEntity = userConverter.formToEntity(dto);
            if(dto.getProfile() != null) {
                Profile savedProfile = profileRepository.save(profileConverter.formToEntitySave(dto.getProfile()));
                userEntity.setProfile(savedProfile);
            }
            User savedUser = userRepository.save(userEntity);
            return userConverter.entityToView(savedUser);
        }

        // User exist --> SignIn operation --> Validate password & Return Created Advertisements by the user
        User foundUser = userRepository.findByEmail(dto.getEmail());
        if(!customPasswordEncoder.matches(dto.getPassword(), foundUser.getPassword()))
            throw new WrongPasswordException("Check your password...");

        Profile savedProfile;
        if(foundUser.getProfile() == null) {
            //Save profile if not in database
            if(dto.getProfile() != null) {
                savedProfile = profileRepository.save(profileConverter.formToEntitySave(dto.getProfile()));
                foundUser.setProfile(savedProfile);
                foundUser = userRepository.save(foundUser);
            }
        }
        else {
            //Update profile if both are different
            if(dto.getProfile() != null) {
                if(!foundUser.getProfile().getCountry().equals(dto.getProfile().getCountry())) {
                    profileRepository.updateProfileById(dto.getProfile().getProfileId(), dto.getProfile().getCountry());
                    foundUser.getProfile().setCountry(dto.getProfile().getCountry());
                }
            }
        }

        List<Advertisement> advertisements = advertisementRepository.findByUser_Email(dto.getEmail());
        foundUser.setAdvertisements(advertisements);
        return userConverter.entityToView(foundUser);
    }

    @Override
    public List<AdvertisementDTOView> retrieveAll() {
        List<Advertisement> advertisementEntities;
        advertisementEntities = advertisementRepository.findAll();
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }

    @Override
    @Transactional
    @Modifying
    public List<AdvertisementDTOView> registerAdvertisement(AdvertisementDTOForm dto) {
        //Advertisement is null --> Throw exception
        if(dto == null)
            throw new IllegalArgumentException("Advertisement Form cannot be null...");

        //SignUp or SignIn
        UserDTOView authenticatedUserView = authenticateUser(dto.getUser());

        //Converting to entities
        Advertisement advertisementEntity = advertisementConverter.formToEntitySave(dto);
        User authenticatedUserEntity = userConverter.viewToEntity(authenticatedUserView);

        //Add Advertisement to User's Advertisement list & Set User to Advertisement --> Then save Advertisement
        authenticatedUserEntity.addAdvertisement(advertisementEntity);
        advertisementRepository.save(advertisementEntity);

        return authenticatedUserEntity.getAdvertisements()
                                        .stream()
                                        .map(advertisementConverter::entityToView)
                                        .toList();
    }

    @Override
    @Transactional
    public List<AdvertisementDTOView> deRegisterAdvertisement(AdvertisementDTOForm dto) {
        //Advertisement is null --> Throw exception
        if(dto == null)
            throw new IllegalArgumentException("Advertisement Form cannot be null...");

        //SignUp or SignIn
        UserDTOView authenticatedUserView = authenticateUser(dto.getUser());

        //Advertisement is not found --> Throw exception
        Advertisement advertisementEntity = advertisementConverter.formToEntityUpdate(dto);
        Optional<Advertisement> foundAdvertisement = advertisementRepository.findById(advertisementEntity.getAdvertisementId());
        if(foundAdvertisement.isEmpty())
            throw new DataNotFoundException("Advertisement not found to delete...");

        User authenticatedUserEntity = userConverter.viewToEntity(authenticatedUserView);
        advertisementEntity.setUser(authenticatedUserEntity);

        //Remove Advertisement in User's Advertisement list & Set Null to User in Advertisement --> Then delete Advertisement
        authenticatedUserEntity.removeAdvertisement(advertisementEntity);
        advertisementRepository.delete(advertisementEntity);

        return authenticatedUserEntity.getAdvertisements()
                .stream()
                .map(advertisementConverter::entityToView)
                .toList();
    }
}
