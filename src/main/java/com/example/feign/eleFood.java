package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080") // CORS 설정 (필요에 따라 조정)
@FeignClient(name = "foodApi", url = "http://elasticsearch.icebuckwheat.kro.kr:9200",configuration = FeignConfig.class)
public interface eleFood {
    @GetMapping("/food/_search")
    String getFoodData(@RequestParam("q") String food);

    @PostMapping(value = "/food/_search" , consumes = "application/json")
    String searchFood(@RequestBody String query);
}
