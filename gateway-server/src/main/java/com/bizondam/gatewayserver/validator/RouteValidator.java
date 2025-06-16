package com.bizondam.gatewayserver.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

//인증이 필요 없는 경로를 정의하는 클래스
@Component
public class RouteValidator {
  public static final List<String> openApiEndpoints = List.of(
      "/api/auth/login",
      "/api/auth/refresh-token",
      "/api/users/register",
      "/api/users/check-login-id",
      "/api/users/email-auth",
      "/api/users/email-auth/verify",
      // Swagger/OpenAPI 관련 경로
      "/swagger-ui.html",
      "/swagger-ui/",
      "/swagger-ui",
      "/swagger-ui/**",
      "/v3/api-docs",
      "/v3/api-docs/",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/",
      "/swagger-resources/**",
      "/webjars/",
      "/webjars/**",
      "/favicon.ico",
      // 각 서비스별로 프록시되는 경로
      "/user-service/v3/api-docs",
      "/company-service/v3/api-docs",
      "/estimate-service/v3/api-docs",
      "/matching-service/v3/api-docs"
  );

  public Predicate<ServerHttpRequest> isSecured =
      request -> openApiEndpoints.stream()
          .noneMatch(uri -> request.getURI().getPath().contains(uri));
}