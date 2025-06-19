package com.bizondam.company_service.client;

import com.bizondam.company_service.config.NationalTaxFeignConfig;
import com.bizondam.company_service.dto.CompanyValidationRequest;
import com.bizondam.company_service.dto.CompanyValidateResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "national-tax-client",
    url = "${national-tax.api-url}",
    configuration = NationalTaxFeignConfig.class,
    fallback = NationalTaxFallback.class
)
public interface NationalTaxClient {
    @PostMapping("/validate")
    CompanyValidateResultResponse validateBusiness(@RequestBody CompanyValidationRequest request);
}