package com.bizondam.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SwaggerConfig {
  @Value("${server.servlet.context.path:}")
  private String contextPath;

  @Bean
  public OpenAPI customOpenAPI(){
    Server localServer = new Server();
    localServer.setUrl(contextPath);
    localServer.setDescription("Local Server");

    return new OpenAPI()
        .addServersItem(localServer)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(new Info().title("Swagger API 명세서").version("1.0").description("My Swagger"));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi(){
    return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain swaggerPermitAllChain(HttpSecurity http) throws Exception {
    http
        // Swagger 관련 경로만 매칭
        .securityMatcher(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
        )
        // CSRF 비활성화(필요에 따라)
        .csrf(csrf -> csrf.disable())
        // 매칭된 모든 요청을 permitAll 처리
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }
}