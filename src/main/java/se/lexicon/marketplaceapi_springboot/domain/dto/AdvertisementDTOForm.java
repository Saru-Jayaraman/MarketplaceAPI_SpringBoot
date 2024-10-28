package se.lexicon.marketplaceapi_springboot.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AdvertisementDTOForm {
    @PositiveOrZero(message = "Id cannot hold negative value")
    private Long advertisementId;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must contain a min of 3 and max of 100 characters")
    private String title;

    @Size(max = 1000, message = "Title can contain max of 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price cannot hold negative value")
    private Double price;

    private LocalDateTime createdDate;

    private LocalDateTime expiredDate;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "City is required")
    private String city;

    @Valid
    private UserDTOForm user;
}
