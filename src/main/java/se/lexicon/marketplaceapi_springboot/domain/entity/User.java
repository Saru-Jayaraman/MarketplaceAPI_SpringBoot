package se.lexicon.marketplaceapi_springboot.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Profile profile;

    public User(String email, String password, Profile profile) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.isExpired = false;
    }

    public void addAdvertisement(Advertisement advertisement) {
        advertisement.setUser(this);
        if(advertisements == null) {
            advertisements = new ArrayList<>();
            advertisements.add(advertisement);
        } else {
            List<Advertisement> newAdvertisements = new ArrayList<>(List.copyOf(advertisements));
            newAdvertisements.add(advertisement);
            advertisements = newAdvertisements;
        }
    }

    public void removeAdvertisement(Advertisement advertisement) {
        if(this.advertisements.contains(advertisement) && advertisement.getUser() == this) {
            advertisement.setUser(null);
            List<Advertisement> newAdvertisements = new ArrayList<>(List.copyOf(advertisements));
            newAdvertisements.remove(advertisement);
            advertisements = newAdvertisements;
        }
    }
}
