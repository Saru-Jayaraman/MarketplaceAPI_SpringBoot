package se.lexicon.marketplaceapi_springboot.converter;

import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.domain.entity.Advertisement;

public interface AdvertisementConverter {
    Advertisement formToEntitySave(AdvertisementDTOForm dto);

    Advertisement formToEntityUpdate(AdvertisementDTOForm dto);

    AdvertisementDTOView entityToView(Advertisement entity);

    Advertisement viewToEntity(AdvertisementDTOView dto);
}
