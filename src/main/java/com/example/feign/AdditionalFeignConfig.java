package com.example.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class AdditionalFeignConfig {

    @Value("${foodsafety.api.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor bearerAuthRequestInterceptor() {
        return requestTemplate -> {
            // Authorization 헤더에 Bearer 토큰 설정
            requestTemplate.header("Authorization", "Bearer " + apiKey);
        };
    }
}
