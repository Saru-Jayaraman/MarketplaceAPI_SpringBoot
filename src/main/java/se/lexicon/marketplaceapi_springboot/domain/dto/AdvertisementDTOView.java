package se.lexicon.marketplaceapi_springboot.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AdvertisementDTOView {
    private Long advertisementId;
    private String title;
    private String description;
    private Double price;
    private LocalDateTime createdDate;
    private LocalDateTime expiredDate;
    private String category;
    private String city;
}
