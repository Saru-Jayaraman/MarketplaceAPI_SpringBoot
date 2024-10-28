package se.lexicon.marketplaceapi_springboot.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ProfileDTOView {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
//    private LocalDate birthDate;
    private LocalDate joinedDate;
}
