package com.bizondam.gatewayserver.filter;

import com.bizondam.common.exception.AuthErrorCode;
import com.bizondam.common.exception.CustomException;
import com.bizondam.common.exception.model.BaseErrorCode;
import com.bizondam.common.jwt.JwtProvider;
import com.bizondam.common.response.BaseResponse;
import com.bizondam.gatewayserver.validator.RouteValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
// 모든 요청에 대해 JWT 토큰의 유효성을 검증하는 Gateway 필터
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
              BaseErrorCode errorCode = ce.getErrorCode();

              // BaseErrorCode가 AuthErrorCode 타입인지 확인 (다른 도메인 에러코드와의 혼동 방지)
              if (errorCode instanceof AuthErrorCode) {
                AuthErrorCode authErrorCode = (AuthErrorCode) errorCode;

                // JWT 토큰 만료 예외 처리
                if (authErrorCode == AuthErrorCode.JWT_TOKEN_EXPIRED) {
                  // 클라이언트에게 액세스 토큰 만료 메시지와 401 상태코드 반환
                  return onError(exchange, "ACCESS_TOKEN_EXPIRED: " + authErrorCode.getMessage(), HttpStatus.UNAUTHORIZED);
                }
                // JWT 토큰이 유효하지 않은 경우 (형식 오류, 서명 오류 등)
                else if (authErrorCode == AuthErrorCode.INVALID_ACCESS_TOKEN ||
                    authErrorCode == AuthErrorCode.UNSUPPORTED_TOKEN ||
                    authErrorCode == AuthErrorCode.MALFORMED_JWT_TOKEN ||
                    authErrorCode == AuthErrorCode.INVALID_SIGNATURE ||
                    authErrorCode == AuthErrorCode.ILLEGAL_ARGUMENT) {
                  // 클라이언트에게 유효하지 않은 토큰 메시지와 401 상태코드 반환
                  return onError(exchange, "INVALID_ACCESS_TOKEN: " + authErrorCode.getMessage(), HttpStatus.UNAUTHORIZED);
                }
              }
              // 만약 AuthErrorCode가 아닌 다른 도메인 에러코드라면, 해당 에러코드의 메시지와 상태코드로 반환
              return onError(exchange, errorCode.getMessage(), errorCode.getStatus());
            }
            // CustomException이 아닌 예외가 발생한 경우, 내부 서버 오류로 처리
            return onError(exchange, "Invalid authentication token", HttpStatus.INTERNAL_SERVER_ERROR);
          });
    };
  }

  private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
    exchange.getResponse().setStatusCode(status);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    ObjectMapper om = new ObjectMapper();
    BaseResponse<Object> errorBody = BaseResponse.fail(401, "JWT 토큰이 만료되었습니다", null);

    try {
      byte[] bytes = om.writeValueAsBytes(errorBody); // JSON 직렬화
      DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
      return exchange.getResponse().writeWith(Mono.just(buffer));
    } catch (Exception e) {
      return exchange.getResponse().setComplete();
    }
  }

  public static class Config {
    public Config() {}
  }
}