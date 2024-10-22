package se.lexicon.marketplaceapi_springboot.converter;

import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;

public interface AdvertisementConverter {
    Advertisement toAdvertisementEntitySave(AdvertisementDTOForm dto);

    Advertisement toAdvertisementEntityUpdate(AdvertisementDTOForm dto);

    AdvertisementDTOView toAdvertisementDTOView(Advertisement entity);
}
