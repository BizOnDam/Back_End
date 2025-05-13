package com.bizondam.company_service.client;

import com.bizondam.company_service.dto.CompanyValidationRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NationalTaxClient {

    @Value("${national-tax.api-url}")
    private String apiUrl;

    @Value("${national-tax.service-key}")
    private String serviceKey;

    // HTTP 요청 처리
    private final RestTemplate restTemplate = new RestTemplate();

    // TODO 컨피그 연결하면 아래 코드로 수정
//    private final RestTemplate restTemplate;

    public boolean verify(CompanyValidationRequestDto dto) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Infuser " + serviceKey);


        Map<String, Object> business = Map.of(
                "b_no", dto.getB_no(),
                "start_dt", dto.getStart_dt(),
                "p_nm", dto.getP_nm(),
                "b_nm", dto.getB_nm()
        );

        // 요청 본문 설정
        Map<String, Object> body = Map.of("businesses", List.of(business));
        // 요청 엔티티 설정
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        System.out.println("▶ 요청 바디: " + body);
        try {
            // API 호출
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    JsonNode.class
            );

            System.out.println("▶ 응답 결과: " + response.getBody());

            // 데이터에서 상태코드 추출 및 확인
            JsonNode validCode = response.getBody()
                    .path("data")
                    .get(0)
                    .path("valid");

            return "01".equals(validCode.asText());

        } catch (HttpClientErrorException e) {
            // 400 Bad Request 발생 시 에러 메시지 출력
            System.err.println("▶ HTTP 요청 에러: " + e.getStatusCode());
            System.err.println("▶ 에러 메시지: " + e.getResponseBodyAsString());
            throw new IllegalStateException("국세청 API 요청 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            // 기타 모든 예외 처리
            System.err.println("▶ 알 수 없는 오류: " + e.getMessage());
            throw new RuntimeException("API 호출 중 문제가 발생했습니다.", e);
        }
    }
}
