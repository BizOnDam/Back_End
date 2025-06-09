package com.bizondam.estimateservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
    "com.bizondam.estimateservice",
    "com.bizondam.common.config"
})
@MapperScan("com.bizondam.estimateservice.mapper")
public class EstimateServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(EstimateServiceApplication.class, args);
  }
}