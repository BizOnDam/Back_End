package com.bizondam.company_service.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "BaseResponse DTO", description = "공통 API 응답 형식")
public class BaseResponse<T> {

  @Schema(description = "요청 성공 여부", example = "true")
  private boolean success;

  @Schema(description = "HTTP 상태 코드", example = "200")
  private String code;

  @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
  private String message;

  @Schema(description = "응답 데이터")
  private T data;

  // 기본 성공 (결과 데이터만)
  public static <T> BaseResponse<T> success(String message, T data) {
    return new BaseResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
  }

  // 메시지·코드까지 지정하고 싶을 때
  public static <T> BaseResponse<T> success(String code, String message, T data) {
    return new BaseResponse<>(true, code, message, data);
  }

  // 에러 코드·메시지
  public static <T> BaseResponse<T> error(String code, String message) {
    return new BaseResponse<>(false, code, message, null);
  }
}