package se.lexicon.marketplaceapi_springboot.converter;

import org.springframework.stereotype.Component;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;

@Component
public class AdvertisementConverterImpl implements AdvertisementConverter {
    @Override
    public Advertisement formToEntitySave(AdvertisementDTOForm dto) {
        return Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .city(dto.getCity())
                .build();
    }

    @Override
    public Advertisement formToEntityUpdate(AdvertisementDTOForm dto) {
        return Advertisement.builder()
                .advertisementId(dto.getAdvertisementId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .createdDate(dto.getCreatedDate())
                .expiredDate(dto.getExpiredDate())
                .category(dto.getCategory())
                .city(dto.getCity())
                .build();
    }

    @Override
    public AdvertisementDTOView entityToView(Advertisement entity) {
        return AdvertisementDTOView.builder()
                .advertisementId(entity.getAdvertisementId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdDate(entity.getCreatedDate())
                .expiredDate(entity.getExpiredDate())
                .category(entity.getCategory())
                .city(entity.getCity())
                .build();
    }

    @Override
    public Advertisement viewToEntity(AdvertisementDTOView dto) {
        return Advertisement.builder()
                .advertisementId(dto.getAdvertisementId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .createdDate(dto.getCreatedDate())
                .expiredDate(dto.getExpiredDate())
                .category(dto.getCategory())
                .city(dto.getCity())
                .build();
    }
}
