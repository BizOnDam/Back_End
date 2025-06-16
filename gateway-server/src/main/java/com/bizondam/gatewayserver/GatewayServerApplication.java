package com.bizondam.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
		org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {"com.bizondam"},
		excludeFilters = {
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.bizondam\\.common\\.exception\\.GlobalExceptionHandler"),
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.bizondam\\.common\\.config\\.SecurityConfig")
		})
public class GatewayServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
}