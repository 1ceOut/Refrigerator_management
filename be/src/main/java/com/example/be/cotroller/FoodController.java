package com.example.be.cotroller;

import com.example.be.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 (필요에 따라 조정)
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/food/detail") // POST 메소드로 변경
    public ResponseEntity<String> getFoodData(@RequestBody Map<String, String> requestBody) {
        String mcategory = requestBody.get("mcategory");
        String scategory = requestBody.get("scategory");

        // 서비스 메소드 호출
        String response = foodService.getNutritionInfo(mcategory, scategory);
        return ResponseEntity.ok(response);
    }
}
