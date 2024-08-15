package com.example.be.service;

import com.example.feign.eleFood;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final eleFood eleFood;

    public FoodService(eleFood eleFood) {
        this.eleFood = eleFood;
    }

    public String getNutritionInfo(String mcategory, String scategory) {
        // JSON 쿼리 문자열 생성
        String query = String.format("{\"query\": {\"bool\": {\"must\": [{\"wildcard\": {\"mcategory\": \"*%s*\"}}, {\"wildcard\": {\"scategory\": \"*%s*\"}}]}}}", mcategory, scategory);
        return eleFood.searchFood(query);
    }
}
