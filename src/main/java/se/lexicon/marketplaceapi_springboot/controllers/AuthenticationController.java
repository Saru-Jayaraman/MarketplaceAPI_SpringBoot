package se.lexicon.marketplaceapi_springboot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.service.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authenticate")
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //User Authentication API:
    @Operation(summary = "SIGN UP - SIGN IN operation and SAVE Profile details of the user",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn and Return User along with the Advertisements --> If user's email exists.\n3. Save Profile details --> If exists inside the user form.\n4. Update Profile details --> If there is change in country name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation and Profile details is also saved."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/asUser")
    public ResponseEntity<UserDTOView> authenticateUser(@RequestBody @Valid UserDTOForm dto) {
        UserDTOView userDTOView = authenticationService.authenticateUser(dto);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOView);
    }

    //Guest Authentication API:
    @Operation(summary = "SIGN IN as GUEST and View all the unexpired advertisements",
            description = "1. SignIn as Guest.\n2. Return only the Unexpired Advertisements.\n3. View all the Unexpired Advertisements without any filter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in as Guest."),
    })
    @GetMapping("/asGuest")
    public ResponseEntity<List<AdvertisementDTOView>> retrieveAll() {
        List<AdvertisementDTOView> advertisementDTOViews = authenticationService.retrieveAll();
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Creation API:
    @Operation(summary = "SIGN UP - SIGN IN operation and REGISTER Advertisement after login",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn --> If user's email exists.\n3. Save Profile details --> If exists inside the user form.\n4. Update Profile details --> If there is change in country name.\n5. Register Advertisement to the database after logging in.\n6. Return the list of Advertisements created by the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is logged in via SignUp/SignIn operation and Register Advertisement to the corresponding User."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/registerAd")
    public ResponseEntity<List<AdvertisementDTOView>> registerAdvertisement(@RequestBody @Valid AdvertisementDTOForm dto) {
        List<AdvertisementDTOView> dtoViews = authenticationService.registerAdvertisement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoViews);
    }

    //Advertisement Deletion API:
    @Operation(summary = "SIGN UP - Throw error since there is NO Advertisement to delete & SIGN IN - DE-REGISTER Advertisement if exists",
            description = "1. SignUp --> If user's email does not exists & Throw error since there is NO Advertisement to delete.\n2. SignIn --> If user's email exists.\n3. Save Profile details --> If exists inside the user form.\n4. Update Profile details --> If there is change in country name.\n5. De-register Advertisement --> If exists in the database after logging in.\n6. Return the list of User's Advertisements after deleting it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation and Delete Advertisement if exists."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/deregisterAd")
    @Transactional
    public ResponseEntity<List<AdvertisementDTOView>> deRegisterAdvertisement(@RequestBody @Valid AdvertisementDTOForm dto) {
        List<AdvertisementDTOView> dtoViews = authenticationService.deRegisterAdvertisement(dto);
        return ResponseEntity.status(HttpStatus.OK).body(dtoViews);
    }
}
