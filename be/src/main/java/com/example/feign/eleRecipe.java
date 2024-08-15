package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "recipeApi", url = "http://elasticsearch.icebuckwheat.kro.kr:9200", configuration = FeignConfig.class)
public interface eleRecipe {
    @PostMapping("/recipe/_search")
    String getRecipeData(@RequestParam("q") String food);

    @PostMapping(value = "/recipe/_search" , consumes = "application/json")
    String searchRecipe(@RequestBody String query);
}
