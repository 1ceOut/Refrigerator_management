package com.example.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Basic Authentication 헤더 설정
            String credentials = "elastic:finalproject";
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            requestTemplate.header("Authorization", "Basic " + encodedCredentials);

            // Content-Type 헤더 설정
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
