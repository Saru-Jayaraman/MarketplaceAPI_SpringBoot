package se.lexicon.marketplaceapi_springboot.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserDTOView {
    private String email;
    private String firstName;
    private String lastName;
    private List<AdvertisementDTOView> advertisements;
}
