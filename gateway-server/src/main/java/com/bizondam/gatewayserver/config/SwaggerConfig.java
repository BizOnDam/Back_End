package com.bizondam.gatewayserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI gatewayOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("BizOnDam Gateway API Docs")
            .version("1.0")
            .description("Gateway에서 통합 제공하는 Swagger UI"));
  }
}