package com.example.be.controller;

import com.example.be.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/food")
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true") // CORS 설정 (필요에 따라 조정)
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/recipe/detail") // POST 메소드로 변경
    public ResponseEntity<String> getFoodData(@RequestBody Map<String, String> requestBody) {
        String recipe_name = requestBody.get("recipe_name");
        String scategory = requestBody.get("scategory");

        // 서비스 메소드 호출
        String response = recipeService.getNutritionInfo(recipe_name, scategory);
        return ResponseEntity.ok(response);
    }
}
