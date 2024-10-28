package se.lexicon.marketplaceapi_springboot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi_springboot.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi_springboot.service.AdvertisementServingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/advertisements")
@Validated
public class AdvertisementServingController {

    private final AdvertisementServingService advertisementServingService;

    @Autowired
    public AdvertisementServingController(AdvertisementServingService advertisementServingService) {
        this.advertisementServingService = advertisementServingService;
    }

    //Advertisement Serving API - Single Filter Option
    @Operation(summary = "FILTER by Category, City Or Price Range",
            description = "1. Filter Type and Filter Value needs to be provided for filtering all the data.\n2. Filter by Category, City and Price range is available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result from filter by Category, City or Price range."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/singleFilterOption")
    public ResponseEntity<List<AdvertisementDTOView>> retrieveByFilterOption
    (
        @RequestParam @NotBlank(message = "Select anyone of the available filter options type") String optionType,
        @RequestParam @NotBlank(message = "Select anyone of the available filter options value") String optionValue
    ) {
        List<AdvertisementDTOView> advertisementDTOViews = advertisementServingService.retrieveByFilterOption(optionType, optionValue);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Serving API - Multiple Filter Option
    @Operation(summary = "FILTER by Any of the combination of Category, City And Price Range",
            description = "1. Category, City, Price Range or Any of its combination needs to be provided for filtering all the data.\n2. Filter by any combination of Category, City and Price range is available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result from filter by Category, City, Price range and its combination."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/multipleFilterOption")
    public ResponseEntity<List<AdvertisementDTOView>> multipleFilterOption(@RequestParam String category, @RequestParam String city, @RequestParam String priceRange) {
        if(category.isEmpty() && city.isEmpty() && priceRange.isEmpty())
            throw new IllegalArgumentException("Select anyone of the filter option... Filter cannot be null...");
        List<AdvertisementDTOView> advertisementDTOViews = advertisementServingService.retrieveByMultipleFilterOption(category, city, priceRange);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }

    //Advertisement Serving API - Order the filtered items By Price/City in ASC/DESC
    @Operation(summary = "ORDER the filtered items by Price/City in ASC/DESC manner",
            description = "1. Category, City, Price Range or Any of its combination needs to be provided for filtering all the data.\n2. Filter by any combination of Category, City and Price range is available.\n3. Order the filtered items by Price Or City.\n4. Order is done in Ascending Or Descending order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provides result based on Ordering by Price/City in Ascending/Descending manner after Filtering by Category, City, Price range or any of its combination."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @GetMapping("/order")
    public ResponseEntity<List<AdvertisementDTOView>> sortAdvertisements
    (
            @RequestParam String category,
            @RequestParam String city,
            @RequestParam String priceRange,
            @RequestParam @NotBlank(message = "Select anyone of the ordering option(Order by price or city)") String orderBy,
            @RequestParam @NotBlank(message = "Select anyone of the ordering manner(Order by asc or desc)") String orderType
    ) {
        if(category.isEmpty() && city.isEmpty() && priceRange.isEmpty())
            throw new IllegalArgumentException("Select anyone of the filter option... Filter cannot be null...");
        List<AdvertisementDTOView> advertisementDTOViews = advertisementServingService.retrieveOrderedAdvertisements(category, city, priceRange, orderBy, orderType);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementDTOViews);
    }
}
