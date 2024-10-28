package se.lexicon.marketplaceapi_springboot.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ProfileDTOForm {
    @PositiveOrZero(message = "Id cannot hold negative value")
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First Name must be between 5 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last Name must be between 5 and 50 characters")
    private String lastName;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Country is required")
    private String country;

//    private LocalDate birthDate;

    private LocalDate joinedDate;
}
