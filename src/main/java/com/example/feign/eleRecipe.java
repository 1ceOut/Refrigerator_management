package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8080") // CORS 설정 (필요에 따라 조정)
@FeignClient(name = "recipeApi", url = "http://elasticsearch.icebuckwheat.kro.kr:9200", configuration = FeignConfig.class)
public interface eleRecipe {
    @PostMapping("/recipe/_search")
    String getRecipeData(@RequestParam("q") String food);

    @PostMapping(value = "/recipe/_search" , consumes = "application/json")
    String searchRecipe(@RequestBody String query);
}
