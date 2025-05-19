package com.bizondam.publicdata_service.client;

import com.bizondam.publicdata_service.dto.ProcurementRequestDto;
import com.bizondam.publicdata_service.dto.ProcurementResponseDto;
import com.bizondam.publicdata_service.dto.ProcurementResponseWrapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcurementClient {

    @Value("${pps-prdctinfo.api-url}")
    private String apiUrl;

    @Value("${pps-prdctinfo.service-key}")
    private String serviceKey;

    @Value("${pps-prdctinfo.return-type}")
    private String returnType;

    private final RestTemplate restTemplate;

    // 조달청 API 호출
    public ProcurementResponseDto fetchContracts(ProcurementRequestDto requestDto) {
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        System.err.println("▶ API 키: " + encodedServiceKey);

        // URI
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(apiUrl)
            .queryParam("ServiceKey", encodedServiceKey)
            .queryParam("type", returnType)
            .queryParam("numOfRows", requestDto.getNumOfRows())
            .queryParam("pageNo",     requestDto.getPageNo())
            .queryParam("inqryDiv",    requestDto.getInqryDiv())
            .queryParam("inqryBgnDate",requestDto.getInqryBgnDate())
            .queryParam("inqryEndDate",requestDto.getInqryEndDate())
            .queryParam("inqryPrdctDiv",requestDto.getInqryPrdctDiv());

        // 조건부 파라미터 추가
        addQueryParamIfPresent(builder, "prdctClsfcNo", requestDto.getPrdctClsfcNo());
        addQueryParamIfPresent(builder, "prdctClsfcNoNm", requestDto.getPrdctClsfcNoNm());
        addQueryParamIfPresent(builder, "dtilPrdctClsfcNo", requestDto.getDtilPrdctClsfcNo());
        addQueryParamIfPresent(builder, "dtilPrdctClsfcNoNm", requestDto.getDtilPrdctClsfcNoNm());
        addQueryParamIfPresent(builder, "prdctIdntNo", requestDto.getPrdctIdntNo());
        addQueryParamIfPresent(builder, "prdctIdntNoNm", requestDto.getPrdctIdntNoNm());
        addQueryParamIfPresent(builder, "prcrmntDiv", requestDto.getPrcrmntDiv());
        addQueryParamIfPresent(builder, "dminsttcD", requestDto.getDminsttcD());
        addQueryParamIfPresent(builder, "dminsttNm", requestDto.getDminsttNm());
        addQueryParamIfPresent(builder, "bizRegNo", requestDto.getBizRegNo());
        addQueryParamIfPresent(builder, "corpNm", requestDto.getCorpNm());
        addQueryParamIfPresent(builder, "fnlCntrctDlvrReqChgOrdYn", requestDto.getFnlCntrctDlvrReqChgOrdYn());
        addQueryParamIfPresent(builder, "exclcProdctYn", requestDto.getExclcProdctYn());
        addQueryParamIfPresent(builder, "cnstwkMtrlDrctPurchsObjYn", requestDto.getCnstwkMtrlDrctPurchsObjYn());


        URI uri = builder
            .build(true)
            .toUri();

        log.info("▶ 호출 URI: {}", uri);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<ProcurementResponseWrapper> entity = new HttpEntity<>(headers);
        ResponseEntity<ProcurementResponseWrapper> response =
                restTemplate.exchange(uri, HttpMethod.GET, entity, ProcurementResponseWrapper.class);

        System.err.println("▶ 응답 원문:\n" + response.getBody());
        System.err.println("▶ 응답 Content-Type: " + response.getHeaders().getContentType());

        ProcurementResponseWrapper wrapper = response.getBody();
        System.err.println("▶ response: " + response);
        if (wrapper == null || wrapper.getResponse() == null || wrapper.getResponse().getBody() == null) {
            log.warn("응답 결과 wrapper or response 없음");
            return null;
        }

        return wrapper.getResponse();
    }

    private void addQueryParamIfPresent(UriComponentsBuilder builder, String key, String value) {
        if (StringUtils.hasText(value)) {
            builder.queryParam(key, value);
        }
    }
}