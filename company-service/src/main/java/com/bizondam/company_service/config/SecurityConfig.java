package com.bizondam.company_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CorsConfig corsConfig;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // CORS 설정
        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

        // CSRF 비활성화 (JWT 기반 REST API의 일반적인 설정)
        .csrf(CsrfConfigurer::disable)

        // 권한 설정
        .authorizeHttpRequests(auth -> auth
            // 정적 리소스 허용
            .requestMatchers(
                "/favicon.ico", "/error"
            ).permitAll()

                .requestMatchers(
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/favicon.ico", "/error"
                ).permitAll()

            // 인증 관련 요청 허용
            .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/api/**").permitAll()

                // 그 외 요청 차단
            .anyRequest().denyAll()
        );

    return httpSecurity.build();
  }
}