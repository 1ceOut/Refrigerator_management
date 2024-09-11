package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "barcodeResult", url = "${foodsafety.api.url}", configuration = AdditionalFeignConfig.class)
public interface BarcodeResultClient {

        @GetMapping("/api/{apiKey}/C005/json/1/5/BAR_CD={barcode}")
        String getFoodSafetyInfo(@PathVariable("apiKey") String apiKey, @PathVariable("barcode") String barcode);

        @GetMapping("/api/{apiKey}/COOKRCP01/json/1/1000")
        String getRecipesByIngredient(@PathVariable("apiKey") String apiKey,@RequestParam("RCP_PARTS_DTLS") String ingredient);


}
