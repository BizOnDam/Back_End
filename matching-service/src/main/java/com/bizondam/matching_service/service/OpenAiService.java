package com.bizondam.matching_service.service;

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

    public String getRecommendation(String jsonPayload) {
        String prompt = """
            다음 JSON 결과를 바탕으로,
            가장 거래 횟수가 높은 기업을 1순위로 추천해 주세요.
            동률일 경우 리드타임이 짧은 기업을 선택합니다.
            1) summary는 
               '견적 요청하신 제품들의 거래 횟수와 리드타임을 고려하여 1순위로 추천하는 기업은 @@입니다.'형식으로 작성하세요.
            2) commonSuppliers와 perItemSuppliers는
                - 데이터가 없으면 빈 배열([])로,
                - 데이터가 있으면 JSON 배열 형태로 그대로 매핑해 주세요.
            (필드 이름: commonSuppliers, perItemSuppliers)

            예시 JSON:
            {
                "summary": "...",
                "commonSuppliers": [],
                "perItemSuppliers": {}
            }

            실제 데이터:
            %s
            """.formatted(jsonPayload);

        // 요청 바디 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
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

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            throw new RuntimeException("OpenAI 요청 실패: " + e.getMessage(), e);
        }
    }
}
