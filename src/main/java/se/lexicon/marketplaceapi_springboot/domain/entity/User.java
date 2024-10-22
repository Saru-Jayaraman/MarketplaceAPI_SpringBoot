package se.lexicon.marketplaceapi_springboot.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class User {
    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Advertisement> advertisements;

    private boolean isExpired;

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
