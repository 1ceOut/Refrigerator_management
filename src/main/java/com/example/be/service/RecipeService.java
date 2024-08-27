package com.example.be.service;

import com.example.feign.eleRecipe;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final eleRecipe eleRecipe;

    public RecipeService(eleRecipe eleRecipe) {
        this.eleRecipe = eleRecipe;
    }

    public String getNutritionInfo(String recipe_name, String scategory) {
        // JSON 쿼리 문자열 생성
        String query = String.format("{\"query\": {\"bool\": {\"must\": [{\"match\": {\"recipe_name\": \"%s\"}}, {\"match\": {\"scategory\": \"%s\"}}]}}}", recipe_name, scategory);
        return eleRecipe.searchRecipe(query);
    }

}
