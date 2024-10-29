package se.lexicon.marketplaceapi_springboot.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Builder
@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advertisementId;

    @Column(length = 100, nullable = false)
    @Setter private String title;

    @Lob
    @Column(length = 1000)
    @Setter private String description;

    @Column(nullable = false)
    @Setter private Double price;

    @Setter private LocalDateTime createdDate;

    @Setter private LocalDateTime expiredDate;

    @Column(nullable = false)
    @Setter private String category;

    @Column(nullable = false)
    @Setter private String city;

    @ManyToOne
    @JoinColumn(name = "email")
    @Setter private User user;

    @PrePersist
    public void initialData() {
        this.createdDate = LocalDateTime.now();
        this.expiredDate = this.createdDate.plusDays(30);
    }

    public Advertisement(String title, String description, Double price, String category, String city, User user) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.city = city;
        this.user = user;
    }
}
