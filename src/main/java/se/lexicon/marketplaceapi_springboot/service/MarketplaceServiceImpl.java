package se.lexicon.marketplaceapi_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi_springboot.converter.AdvertisementConverterImpl;
import se.lexicon.marketplaceapi_springboot.converter.UserConverterImpl;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;
import se.lexicon.marketplaceapi_springboot.exception.WrongPasswordException;
import se.lexicon.marketplaceapi_springboot.repository.AdvertisementRepository;
import se.lexicon.marketplaceapi_springboot.repository.UserRepository;
import se.lexicon.marketplaceapi_springboot.util.CustomPasswordEncoder;

import java.util.List;

@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserConverterImpl userConverter;
    private final AdvertisementConverterImpl advertisementConverter;
    private final CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    public MarketplaceServiceImpl(UserRepository userRepository, AdvertisementRepository advertisementRepository, UserConverterImpl userConverter, AdvertisementConverterImpl advertisementConverter, CustomPasswordEncoder customPasswordEncoder) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.userConverter = userConverter;
        this.advertisementConverter = advertisementConverter;
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    public UserDTOView authenticateUser(UserDTOForm dto) {
        if(dto == null)
            throw new IllegalArgumentException("User Form cannot be null...");
        // User not exist --> SignUp operation --> Create user in User database
        if(!userRepository.existsByEmail(dto.getEmail())) {
            User userEntity = userConverter.formToEntity(dto);
            User savedUser = userRepository.save(userEntity);
            return userConverter.entityToView(savedUser);
        }
        // User exist --> SignIn operation --> Validate password & Return Created Advertisements by the user
        User foundUser = userRepository.findByEmail(dto.getEmail());
        if(!customPasswordEncoder.matches(dto.getPassword(), foundUser.getPassword()))
            throw new WrongPasswordException("Check your password...");
        List<Advertisement> advertisements = advertisementRepository.findByUser_Email(dto.getEmail());
        foundUser.setAdvertisements(advertisements);
        return userConverter.entityToView(foundUser);
    }

    @Override
    public UserDTOView registerAdvertisement(UserDTOForm userDTO) {
        //SignUp or SignIn
        UserDTOView authenticatedUserView = authenticateUser(userDTO);
        if(userDTO.getAdvertisement() == null)
            throw new IllegalArgumentException("Advertisement Form cannot be null...");

        //Set User to Advertisement --> Then save Advertisement
        Advertisement advertisementEntity = advertisementConverter.formToEntitySave(userDTO.getAdvertisement());
        User authenticatedUser = userConverter.viewToEntity(authenticatedUserView);
        advertisementEntity.setUser(authenticatedUser);
        advertisementRepository.save(advertisementEntity);

        //Get all the Advertisements of the User --> Set Advertisements to User --> Then return UserDTOView
        List<Advertisement> advertisements = advertisementRepository.findByUser_Email(authenticatedUser.getEmail());
        authenticatedUser.setAdvertisements(advertisements);
        return userConverter.entityToView(authenticatedUser);
    }
}
