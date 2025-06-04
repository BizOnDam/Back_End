package com.bizondam.company_service;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.bizondam.company_service.client")
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
		"com.bizondam.company_service",
		"com.bizondam.common.config"
})
public class CompanyServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompanyServiceApplication.class, args);
	}
}