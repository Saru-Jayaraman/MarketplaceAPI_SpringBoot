package se.lexicon.marketplaceapi_springboot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.service.MarketplaceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @Autowired
    public MarketplaceController(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    //User Authentication API:
    @Operation(summary = "SIGN UP & SIGN IN operation",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn and return User along with the created Advertisements --> If user's email exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/authenticate/asUser")
    public ResponseEntity<UserDTOView> authenticateUser(@RequestBody @Valid UserDTOForm dto) {
        UserDTOView userDTOView = marketplaceService.authenticateUser(dto);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOView);
    }

    //Guest Authentication API:
    @Operation(summary = "SIGN IN as GUEST and View all the advertisements without any filter",
            description = "1. SignIn as Guest User.\n2.View all the advertisements without any filter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in as Guest."),
    })
    @GetMapping("/authenticate/asGuest")
    public ResponseEntity<List<AdvertisementDTOView>> retrieveAll() {
        List<AdvertisementDTOView> advertisementDTOViews = marketplaceService.retrieveAll();
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Creation API:
    @Operation(summary = "SIGN UP - SIGN IN operation & REGISTER Advertisement after login",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn --> If user's email exists.\n3. Register Advertisement to the database after logging in.\n4. Return User along with the created Advertisements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is logged in via SignUp/SignIn operation and Register Advertisement to the corresponding User."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/advertisements/register")
    public ResponseEntity<UserDTOView> registerAdvertisement(@RequestBody @Valid UserDTOForm userDTO) {
        UserDTOView userDTOView = marketplaceService.registerAdvertisement(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTOView);
    }

    //Advertisement Deletion API:
    @Operation(summary = "SIGN UP - Throw error since there is NO Advertisement to delete & SIGN IN - DE-REGISTER Advertisement if exists",
            description = "1. SignUp --> If user's email does not exists & Throw error since there is NO Advertisement to delete.\n2. SignIn --> If user's email exists.\n3. De-register Advertisement if exists in the database after logging in.\n4. Return User along with the removed Advertisements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation and Delete Advertisement if exists."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/advertisements/deregister")
    public ResponseEntity<UserDTOView> deRegisterAdvertisement(@RequestBody @Valid UserDTOForm userDTO) {
        UserDTOView userDTOView = marketplaceService.deRegisterAdvertisement(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOView);
    }

    //Advertisement Serving API - Single Filter Option
    @Operation(summary = "FILTER by Category, City Or Price Range",
            description = "1. Filter Type and Filter Value needs to be provided for filtering all the data.\n2. Filter by Category, City and Price range is available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result from filter by Category, City or Price range."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/advertisements/singleFilterOption")
    public ResponseEntity<List<AdvertisementDTOView>> retrieveByFilterOption
    (
        @RequestParam @NotBlank(message = "Select anyone of the available filter options type") String optionType,
        @RequestParam @NotBlank(message = "Select anyone of the available filter options value") String optionValue
    ) {
        List<AdvertisementDTOView> advertisementDTOViews = marketplaceService.retrieveByFilterOption(optionType, optionValue);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Serving API - Multiple Filter Option
    @Operation(summary = "FILTER by Any of the combination of Category, City And Price Range",
            description = "1. Category, City, Price Range or Any of its combination needs to be provided for filtering all the data.\n2. Filter by any combination of Category, City and Price range is available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result from filter by Category, City, Price range and its combination."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/advertisements/multipleFilterOption")
    public ResponseEntity<List<AdvertisementDTOView>> multipleFilterOption(@RequestParam String category, @RequestParam String city, @RequestParam String priceRange) {
        if(category.isEmpty() && city.isEmpty() && priceRange.isEmpty())
            throw new IllegalArgumentException("Select anyone of the filter option... Filter cannot be null...");
        List<AdvertisementDTOView> advertisementDTOViews = marketplaceService.retrieveByMultipleFilterOption(category, city, priceRange);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Serving API - Order the filtered items By Price/City in ASC/DESC
    @Operation(summary = "ORDER the filtered items by Price/City in ASC/DESC manner",
            description = "1. Category, City, Price Range or Any of its combination needs to be provided for filtering all the data.\n2. Filter by any combination of Category, City and Price range is available.\n3. Order the filtered items by Price Or City.\n4. Order is done in Ascending Or Descending order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result based on Ordering by Price/City in Ascending/Descending manner after Filtering by Category, City, Price range or any of its combination."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/advertisements/order")
    public ResponseEntity<List<AdvertisementDTOView>> sortAdvertisements
    (
            @RequestParam String category,
            @RequestParam String city,
            @RequestParam String priceRange,
            @RequestParam @NotBlank(message = "Select anyone of the ordering option(Order by price or city)") String orderBy,
            @RequestParam @NotBlank(message = "Select anyone of the ordering manner(Order by asc or desc)") String orderType
    ) {
        if(category.isEmpty() && city.isEmpty() && priceRange.isEmpty())
            throw new IllegalArgumentException("Select anyone of the filter option... Filter cannot be null...");
        List<AdvertisementDTOView> advertisementDTOViews = marketplaceService.retrieveOrderedAdvertisements(category, city, priceRange, orderBy, orderType);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }
}
