package se.lexicon.marketplaceapi_springboot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi_springboot.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "SIGN UP & SIGN IN operation",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn and return User along with the created Advertisements --> If user's email exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/users")
    public ResponseEntity<UserDTOView> authenticateUser(@RequestBody @Valid UserDTOForm dto) {
        UserDTOView userDTOView = userService.authenticateUser(dto);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOView);
    }

    @Operation(summary = "SIGN UP - SIGN IN operation & REGISTER Advertisement after login",
            description = "1. SignUp --> If user's email does not exists.\n2. SignIn --> If user's email exists.\n3. Register Advertisement to the database after logging in.\n4. Return User along with the created Advertisements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in via SignUp/SignIn operation and Register Advertisement to the corresponding User."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PostMapping("/advertisements")
    public ResponseEntity<UserDTOView> registerAdvertisement(@RequestBody @Valid UserDTOForm userDTO) {
        UserDTOView userDTOView = userService.registerAdvertisement(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOView);
    }
}
