package se.lexicon.marketplaceapi_springboot.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(length = 50, nullable = false)
    @Setter private String firstName;

    @Column(length = 50, nullable = false)
    @Setter private String lastName;

    @Column(length = 10, nullable = false)
    @Setter private String gender;

    @Column(length = 50, nullable = false)
    @Setter private String country;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Setter private LocalDate birthDate;

    @Setter private LocalDate joinedDate;

    @PrePersist
    public void initialData() {
        this.joinedDate = LocalDate.now();
    }

    public Profile(String firstName, String lastName, String gender, String country, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.country = country;
        this.birthDate = birthDate;
    }
}
