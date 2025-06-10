package com.bizondam.matching_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class OpenAiService {
    private final WebClient client;

    public OpenAiService(
            @Value("${openai.api.key}") String apiKey,
            WebClient.Builder webClientBuilder
    ) {
        this.client = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public Mono<String> getRecommendation(String jsonPayload) {
        String userPrompt = """
            다음 JSON 결과를 바탕으로,
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

        return client.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4o-mini",
                        "messages", List.of(
                                Map.of("role","system","content","당신은 조달 매칭 서비스 추천 어시스턴트입니다."),
                                Map.of("role","user","content", userPrompt)
                        )
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(root -> root
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText()
                )
                // 60초 안에 응답 안 오면 타임아웃
                .timeout(Duration.ofSeconds(60))
                // 타임아웃 시 예외를 좀 더 명확히 바꿔 던지기
                .onErrorMap(TimeoutException.class,
                        e -> new RuntimeException("OpenAI 응답 시간이 60초를 초과했습니다.", e)
                );
    }
}
