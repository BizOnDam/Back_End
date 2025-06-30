package com.bizondam.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .authorizeExchange(exchange -> exchange
            .pathMatchers(
                "/api/auth/login",
                "/api/auth/reissue-access-token",
                "/api/auth/reissue-refresh-token",
                "/api/users/register",
                "/api/users/check-login-id",
                "/api/users/email-auth/signup",
                "/api/users/email-auth/find-id",
                "/api/users/email-auth/verify",
                "/api/users/reset-password",
                "/api/users/find-email",
                "/api/users/find-id",
                "/api/companies/register",
                "/api/companies/validate",
                // Swagger/OpenAPI 관련 경로
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/favicon.ico",
                // 각 서비스별로 프록시되는 경로
                "/user-service/v3/api-docs",
                "/company-service/v3/api-docs",
                "/estimate-service/v3/api-docs",
                "/matching-service/v3/api-docs",
                "/contract-service/v3/api-docs"
            ).permitAll()
            .anyExchange().permitAll()
        );
    return http.build();
  }
}