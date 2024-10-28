package se.lexicon.marketplaceapi_springboot.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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
    private Long profileId;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    private LocalDate joinedDate;
}
