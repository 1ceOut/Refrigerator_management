package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "foodApi", url = "http://elasticsearch.icebuckwheat.kro.kr:9200",configuration = FeignConfig.class)
public interface eleFood {
    @GetMapping("/food/_search")
    String getFoodData(@RequestParam("q") String food);

    @PostMapping(value = "/food/_search" , consumes = "application/json")
    String searchFood(@RequestBody String query);
}
