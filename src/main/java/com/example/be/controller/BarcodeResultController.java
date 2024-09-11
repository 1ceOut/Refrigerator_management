package com.example.be.controller;

import com.example.be.service.BarcodeResultService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class BarcodeResultController {

    private final BarcodeResultService barcodeResultService;

    // 바코드로 식품 안전 정보 가져오기
    @GetMapping("/barcode/result")
    public String getFoodSafetyInfo(@RequestParam String barcode) {
        return barcodeResultService.getFoodSafetyInfo(barcode);
    }

    // 재료로 레시피 찾기
    @GetMapping("/find/recipes")
    public List<Map<String, Object>> getRecipes(@RequestParam("ingredient") String ingredient) {
        System.out.println("Ingredient: " + ingredient);
        String response = barcodeResultService.getRecipesByIngredient(ingredient);

        // API 응답 확인
        System.out.println("API Response: " + response);

        List<Map<String, Object>> filteredRecipes = new ArrayList<>();

        try {
            // 응답 데이터를 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode rowNode = rootNode.path("COOKRCP01").path("row");

            // 각 레시피를 순회하며 필요한 데이터 추출
            for (JsonNode recipeNode : rowNode) {
                String recipeName = recipeNode.path("RCP_NM").asText();
                String recipeIngredients = recipeNode.path("RCP_PARTS_DTLS").asText();

                // 재료 목록에서 검색어가 포함된 레시피만 필터링
                if (recipeIngredients.contains(ingredient)) {
                    Map<String, Object> recipe = new HashMap<>();
                    recipe.put("RCP_SEQ", recipeNode.path("RCP_SEQ").asText(null));  // 일련번호
                    recipe.put("RCP_NM", recipeNode.path("RCP_NM").asText(null));  // 메뉴명
                    recipe.put("RCP_WAY2", recipeNode.path("RCP_WAY2").asText(null));  // 조리방법
                    recipe.put("RCP_PAT2", recipeNode.path("RCP_PAT2").asText(null));  // 요리종류
                    recipe.put("INFO_WGT", recipeNode.path("INFO_WGT").asText(null));  // 중량
                    recipe.put("INFO_ENG", recipeNode.path("INFO_ENG").asText(null));  // 열량
                    recipe.put("INFO_CAR", recipeNode.path("INFO_CAR").asText(null));  // 탄수화물
                    recipe.put("INFO_PRO", recipeNode.path("INFO_PRO").asText(null));  // 단백질
                    recipe.put("INFO_FAT", recipeNode.path("INFO_FAT").asText(null));  // 지방
                    recipe.put("INFO_NA", recipeNode.path("INFO_NA").asText(null));  // 나트륨
                    recipe.put("HASH_TAG", recipeNode.path("HASH_TAG").asText(null));  // 해쉬태그
                    recipe.put("ATT_FILE_NO_MAIN", recipeNode.path("ATT_FILE_NO_MAIN").asText(null));  // 이미지경로(소)
                    recipe.put("ATT_FILE_NO_MK", recipeNode.path("ATT_FILE_NO_MK").asText(null));  // 이미지경로(대)
                    recipe.put("RCP_PARTS_DTLS", recipeNode.path("RCP_PARTS_DTLS").asText(null));  // 재료정보

                    // 만드는 법 및 이미지 1~20까지 추가
                    for (int i = 1; i <= 20; i++) {
                        String manualKey = String.format("MANUAL%02d", i);
                        String manualImgKey = String.format("MANUAL_IMG%02d", i);

                        recipe.put(manualKey, recipeNode.path(manualKey).asText(null));  // 만드는법
                        recipe.put(manualImgKey, recipeNode.path(manualImgKey).asText(null));  // 만드는법 이미지
                    }

                    recipe.put("RCP_NA_TIP", recipeNode.path("RCP_NA_TIP").asText(null));  // 저감 조리법 TIP

                    filteredRecipes.add(recipe);
                } else {
                    System.out.println("Filtered out: " + recipeName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 필터링된 레시피가 없으면 null 반환
        return filteredRecipes.isEmpty() ? null : filteredRecipes;
    }
}
