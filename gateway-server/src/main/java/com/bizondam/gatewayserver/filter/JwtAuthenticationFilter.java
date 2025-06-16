package com.bizondam.gatewayserver.filter;

import com.bizondam.common.exception.CustomException;
import com.bizondam.common.jwt.JwtProvider;
import com.bizondam.gatewayserver.validator.RouteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

// 모든 요청에 대해 JWT 토큰의 유효성을 검증하는 Gateway 필터.
// 인증이 필요 없는 경로는 RouteValidator로 건너뜀
// 토큰이 유효하지 않으면 커스텀 예외를 통해 401 반환
@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

  private final JwtProvider jwtProvider;
  private final RouteValidator routeValidator;

  public JwtAuthenticationFilter(JwtProvider jwtProvider, RouteValidator routeValidator) {
    super(Config.class);
    this.jwtProvider = jwtProvider;
    this.routeValidator = routeValidator;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      // 1. 인증이 필요 없는 경로는 바로 통과
      if (!routeValidator.isSecured.test(exchange.getRequest())) {
        return chain.filter(exchange);
      }

      // 2. Authorization 헤더에서 토큰 추출
      String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
      }

      String token = authHeader.substring(7);

      // 3. JWT 토큰 검증 및 claims 추출 (블로킹 코드이므로 별도 스레드에서 실행)
      return Mono.fromCallable(() -> jwtProvider.getClaims(token))
          .subscribeOn(Schedulers.boundedElastic())
          .flatMap(claims -> {
            // 4. Downstream 서비스로 claims(예: userId, role) 전달
            // - 헤더에 추가하여 마이크로서비스 간 사용자 정보 전달
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", claims.get("userId", String.class))
                .header("X-User-Role", claims.get("role", String.class))
                .build();
            // 변경된 요청을 포함한 exchange로 체인 계속 진행
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
          })
          .onErrorResume(e -> {
            // 5. 예외 발생 시 커스텀 예외 메시지와 상태 코드 반환
            if (e instanceof CustomException) {
              CustomException ce = (CustomException) e;
              return onError(exchange, ce.getErrorCode().getMessage(), ce.getErrorCode().getStatus());
            }
            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
          });
    };
  }

  private Mono<Void> onError(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus) {
    log.warn("JWT 인증 실패: {}", errorMsg);
    exchange.getResponse().setStatusCode(httpStatus);
    return exchange.getResponse().setComplete();
  }

  public static class Config {
    public Config() {}
  }
}