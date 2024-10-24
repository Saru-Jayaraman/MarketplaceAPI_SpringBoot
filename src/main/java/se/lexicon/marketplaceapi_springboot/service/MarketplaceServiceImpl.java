package se.lexicon.marketplaceapi_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi_springboot.converter.AdvertisementConverterImpl;
import se.lexicon.marketplaceapi_springboot.converter.UserConverterImpl;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;
import se.lexicon.marketplaceapi_springboot.domain.entity.User;
import se.lexicon.marketplaceapi_springboot.exception.DataNotFoundException;
import se.lexicon.marketplaceapi_springboot.exception.WrongPasswordException;
import se.lexicon.marketplaceapi_springboot.repository.AdvertisementRepository;
import se.lexicon.marketplaceapi_springboot.repository.UserRepository;
import se.lexicon.marketplaceapi_springboot.util.CustomPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        //Advertisement is null --> Throw exception
        if(userDTO.getAdvertisement() == null)
            throw new IllegalArgumentException("Advertisement Form cannot be null...");

        //Converting to entities
        Advertisement advertisementEntity = advertisementConverter.formToEntitySave(userDTO.getAdvertisement());
        User authenticatedUserEntity = userConverter.viewToEntity(authenticatedUserView);

        //Add Advertisement to User's Advertisement list & Set User to Advertisement --> Then save Advertisement
        authenticatedUserEntity.addAdvertisement(advertisementEntity);
        advertisementRepository.save(advertisementEntity);

        return userConverter.entityToView(authenticatedUserEntity);
    }

    @Override
    public UserDTOView deRegisterAdvertisement(UserDTOForm userDTO) {
        //SignUp or SignIn
        UserDTOView authenticatedUserView = authenticateUser(userDTO);

        //Advertisement is null --> Throw exception
        if(userDTO.getAdvertisement() == null)
            throw new IllegalArgumentException("Advertisement Form cannot be null...");

        //Advertisement is not found --> Throw exception
        Advertisement advertisementEntity = advertisementConverter.formToEntityUpdate(userDTO.getAdvertisement());
        Optional<Advertisement> foundAdvertisement = advertisementRepository.findById(advertisementEntity.getAdvertisementId());
        if(foundAdvertisement.isEmpty())
            throw new DataNotFoundException("Advertisement not found to delete...");

        User authenticatedUserEntity = userConverter.viewToEntity(authenticatedUserView);
        advertisementEntity.setUser(authenticatedUserEntity);

        //Remove Advertisement in User's Advertisement list & Set Null to User in Advertisement --> Then delete Advertisement
        authenticatedUserEntity.removeAdvertisement(advertisementEntity);
        advertisementRepository.delete(advertisementEntity);

        return userConverter.entityToView(authenticatedUserEntity);
    }

    @Override
    public List<AdvertisementDTOView> retrieveByFilterOption(String optionType, String optionValue) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(optionType.equalsIgnoreCase("category")) {
            advertisementEntities = advertisementRepository.findByCategory(optionValue);
        } else if(optionType.equalsIgnoreCase("city")) {
            advertisementEntities = advertisementRepository.findByCity(optionValue);
        } else if(optionType.equalsIgnoreCase("price")) {
            String[] priceArray = optionValue.trim().split("-");
            advertisementEntities = advertisementRepository.findByPriceBetween(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
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
    public List<AdvertisementDTOView> retrieveByMultipleFilterOption(String category, String city, String priceRange) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(!category.isEmpty() && !city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetween(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty() && !city.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCategoryAndCity(category, city);
        } else if(!city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCityAndPriceBetween(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByCategoryAndPriceBetween(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        } else if(!category.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCategory(category);
        } else if(!city.isEmpty()) {
            advertisementEntities = advertisementRepository.findByCity(city);
        } else if(!priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            advertisementEntities = advertisementRepository.findByPriceBetween(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }

    @Override
    public List<AdvertisementDTOView> retrieveOrderedAdvertisements(String category, String city, String priceRange, String orderBy, String orderType) {
        List<Advertisement> advertisementEntities = new ArrayList<>();
        if(!category.isEmpty() && !city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceAsc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByPriceDesc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityAsc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityAndPriceBetweenOrderByCityDesc(category, city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty() && !city.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByPriceAsc(category, city);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByPriceDesc(category, city);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByCityAsc(category, city);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndCityOrderByCityDesc(category, city);
            }
        } else if(!city.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByPriceAsc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByPriceDesc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByCityAsc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityAndPriceBetweenOrderByCityDesc(city, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty() && !priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceAsc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByPriceDesc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityAsc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryAndPriceBetweenOrderByCityDesc(category, Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        } else if(!category.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByPriceAsc(category);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByPriceDesc(category);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByCityAsc(category);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCategoryOrderByCityDesc(category);
            }
        } else if(!city.isEmpty()) {
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByPriceAsc(city);
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByPriceDesc(city);
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByCityAsc(city);
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByCityOrderByCityDesc(city);
            }
        } else if(!priceRange.isEmpty()) {
            String[] priceArray = priceRange.trim().split("-");
            if(orderBy.equals("price") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByPriceAsc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("price") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByPriceDesc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("asc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByCityAsc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            } else if(orderBy.equals("city") && orderType.equals("desc")) {
                advertisementEntities = advertisementRepository.findByPriceBetweenOrderByCityDesc(Double.valueOf(priceArray[0]), Double.valueOf(priceArray[1]));
            }
        }
        //Remove Expired Advertisements
        advertisementEntities.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
        return advertisementEntities.stream().map(advertisementConverter::entityToView).toList();
    }
}
