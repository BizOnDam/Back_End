package com.bizondam.publicdata_service.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.bizondam.publicdata_service.client.ProcurementResponseErrorHandler;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 1) XmlMapper 세팅
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 알 수 없는 속성(Fail on unknown)을 끔

        // 2) XML 메시지 컨버터 생성
        MappingJackson2XmlHttpMessageConverter xmlConverter =
                new MappingJackson2XmlHttpMessageConverter(xmlMapper);

        // 3) RestTemplate에 컨버터 등록
        RestTemplate restTemplate = new RestTemplate();
        // 기본 컨버터들 앞에 XML 컨버터를 추가
        restTemplate.getMessageConverters().add(0, xmlConverter);

        return restTemplate;

//        return builder
//                .messageConverters(xmlConv)  // XML 컨버터 등록
//                .build();
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter xmlHttpMessageConverter() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    }


//    @Bean
////    @Primary
//    public RestTemplate restTemplate(RestTemplateBuilder builder, ProcurementResponseErrorHandler errorHandler) {
//        return builder
//                .errorHandler(errorHandler)
////                .additionalMessageConverters(new Jaxb2RootElementHttpMessageConverter()) // XML 컨버터 추가
//                .build();
//    }
}
