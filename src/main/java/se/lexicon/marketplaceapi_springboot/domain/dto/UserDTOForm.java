package se.lexicon.marketplaceapi_springboot.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "advertisement")
@EqualsAndHashCode(exclude = "advertisement")
@Builder
public class UserDTOForm {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lower case letter, " +
                    "one number and one special character")
    private String password;

    @Valid
    private ProfileDTOForm profile;

    @Valid
    private AdvertisementDTOForm advertisement;
}
