package com.bizondam.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface SwaggerConfigSupport {
  default OpenAPI baseOpenAPI(String title, String description) {
    return new OpenAPI()
        .info(new Info().title(title).description(description).version("1.0.0"));
  }
}