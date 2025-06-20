package com.bizondam.company_service.config;

import com.bizondam.common.config.CommonSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    CommonSecurityConfig.applyCommon(http);

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
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
                "/matching-service/v3/api-docs"
            ).permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().permitAll()
        );
    return http.build();
  }
}