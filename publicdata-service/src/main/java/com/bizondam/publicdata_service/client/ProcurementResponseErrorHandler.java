package com.bizondam.publicdata_service.client;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
@Component
public class ProcurementResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode status = response.getStatusCode();
        return status.is4xxClientError() || status.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode status = response.getStatusCode();
        String body = new String(response.getBody().readAllBytes());
        if (status.is4xxClientError()) {
            log.error("▶ 조달청 API 4xx 에러: {} / ▶ 응답 바디: {}", status.value(), body);
            throw new IllegalArgumentException("▶ 조달청 API 클라이언트 오류: " + status.value());
        } else if (status.is5xxServerError()) {
            log.error("▶ 조달청 API 5xx 에러: {} / ▶ 응답 바디: {}", status.value(), body);
            throw new IllegalStateException("▶ 조달청 API 서버 오류: " + status.value());
        }
        // TODO 나중에 더 추가히기
    }
}
