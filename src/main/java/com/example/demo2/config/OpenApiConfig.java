package com.example.demo2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("智慧社區 API 文件").version("0.1.0")
                .description("這是使用 SpringDoc 生成的 Swagger 文件").termsOfService("https://example.com/terms")
                .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
