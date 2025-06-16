package com.bizondam.company_service.config;

import com.bizondam.common.config.SwaggerConfigSupport;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Company-Service API 명세서",
        description = "회사 서비스 API 명세서",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig implements SwaggerConfigSupport {

  @Bean
  public OpenAPI openAPI() {
    SecurityScheme securityScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization");

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

    return baseOpenAPI("Company-Service API 명세서", "회사 서비스 API 명세서")
        .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
        .addSecurityItem(securityRequirement);
  }
}