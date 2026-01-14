package com.bizondam.matching_service.service;

import com.bizondam.matching_service.dto.OpenAiRecommendationDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate(); // 직접 생성하거나 @Bean 등록 가능
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAiRecommendationDto getRecommendation(String jsonPayload) {
        String prompt = """
            다음 JSON 결과를 바탕으로,
            가장 거래 횟수가 높은 기업을 1순위로 추천해 주세요.
            동률일 경우, 리드타임이 짧은 기업을 선택합니다.
            
            반드시 아래 형식으로 응답해 주세요:
            
            1. "summary" 필드는 아래 예시와 같은 형식의 **한 문장**이어야 합니다:
               - 예: "견적 요청하신 제품들의 거래 횟수와 리드타임을 고려하여 1순위로 추천하는 기업은 @@입니다."

            2. "commonSuppliers"와 "perItemSuppliers"는 **JSON 배열**로 표현하며, 
                - 거래 횟수 → 리드타임 기준 **내림차순 정렬**하고,
                - **추천 1순위 기업은 배열의 첫 번째에 위치**시켜야 합니다.

            3. 아래와 같은 최종 JSON 객체 형태로만 응답하세요:

            {
              "summary": "견적 요청하신 제품들의 거래 횟수와 리드타임을 고려하여 1순위로 추천하는 기업은 @@입니다.",
              "commonSuppliers": [...],
              "perItemSuppliers": {
                "productId1": [...],
                "productId2": [...]
              }
            }

            4. 불필요한 설명, 주석, 자연어 해석 없이 **정확히 위 JSON 형식만 출력하세요.**

            실제 데이터:
            %s
            """.formatted(jsonPayload);

        // 요청 바디 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 조달 매칭 서비스 추천 어시스턴트입니다."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            String content = objectMapper
                    .readTree(response.getBody())
                    .path("choices").get(0)
                    .path("message").path("content")
                    .asText();

            // OpenAI 응답을 다시 파싱해서 DTO로 변환
            return objectMapper.readValue(content, OpenAiRecommendationDto.class);
//
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 요청 실패: " + e.getMessage(), e);
        }
    }
}
