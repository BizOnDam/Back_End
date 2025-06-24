package com.bizondam.contract_service.config;

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
                title = "Contract-Service API 명세서",
                description = "계약 서비스 API 명세서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig implements SwaggerConfigSupport {
    @Bean
    public OpenAPI openAPI() {
        // JWT 인증 스키마 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        // 공통 메타정보 + 서비스별 보안 스키마 추가
        return baseOpenAPI("Contract-Service API 명세서", "계약 서비스 API 명세서")
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}