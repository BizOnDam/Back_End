package com.bizondam.gatewayserver.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

//인증이 필요 없는 경로를 정의하는 클래스
@Component
public class RouteValidator {
  public static final List<String> openApiEndpoints = List.of(
      "/user-service/api/auth/login",
      "/user-service/api/auth/reissue-access-token",
      "/user-service/api/auth/reissue-refresh-token",
      "/user-service/api/users/register",
      "/user-service/api/users/check-login-id",
      "/user-service/api/users/email-auth",
      "/user-service/api/users/email-auth/verify",
      "/company-service/api/companies/register",
      "/company-service/api/companies/validate",
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
      "/user-service/v3/api-docs",
      "/company-service/v3/api-docs",
      "/estimate-service/v3/api-docs",
      "/matching-service/v3/api-docs",
      "/contract-service/v3/api-docs"
  );

  public Predicate<ServerHttpRequest> isSecured =
      request -> openApiEndpoints.stream()
          .noneMatch(uri -> request.getURI().getPath().startsWith(uri));
}