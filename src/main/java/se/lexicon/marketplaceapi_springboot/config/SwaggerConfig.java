package se.lexicon.marketplaceapi_springboot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "MarketPlace API", version = "0.1",
        description = "Marketplace API where Signed in User can manage their own Advertisements and View all of the published Advertisements across Sweden"))
public class SwaggerConfig {

}
