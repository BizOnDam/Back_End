package com.bizondam.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
    "com.bizondam.userservice",
    "com.bizondam.common.config"
})
@MapperScan(
    basePackages = "com.bizondam.userservice.mapper",    // SQL 매퍼만 있는 패키지
    annotationClass = org.apache.ibatis.annotations.Mapper.class
)
public class UserServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }
}
