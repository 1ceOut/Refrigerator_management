package com.example.be.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "recipeApi", url = "http://101.79.10.196:9200")
public interface eleRecipe {
    @GetMapping("/api/recipe-endpoint")
    String getData(@RequestParam String param);
}
