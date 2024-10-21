package se.lexicon.marketplaceapi_springboot.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String email;

    @Column(length = 50, nullable = false)
    @Setter private String firstName;

    @Column(length = 50, nullable = false)
    @Setter private String lastName;

    @Column(length = 50, nullable = false)
    @Setter private String password;

    @OneToMany(mappedBy = "user")
    @Setter private List<Advertisement> advertisements;

    @Setter private boolean isExpired;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isExpired = false;
    }

    public void addAdvertisement(Advertisement advertisement) {
        if(advertisement == null)
            throw new IllegalArgumentException("Advertisement is null...");
        if(advertisements == null)
            advertisements = new ArrayList<>();
        advertisement.setUser(this);
        this.advertisements.add(advertisement);
    }

    public void removeAdvertisement(Advertisement advertisement) {
        if(advertisement == null)
            throw new IllegalArgumentException("Advertisement is null...");
        if(this.advertisements.contains(advertisement) && advertisement.getUser() == this) {
            advertisement.setUser(null);
            this.advertisements.remove(advertisement);
        }
    }

    public void removeExpiredAdvertisements() {
        this.advertisements.removeIf(
                advertisement -> advertisement.getExpiredDate().isBefore(LocalDateTime.now())
        );
    }
}
