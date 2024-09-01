package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "barcodeResult", url = "${foodsafety.api.url}", configuration = AdditionalFeignConfig.class)
public interface BarcodeResultClient {

        @GetMapping("/api/{apiKey}/C005/json/1/5/BAR_CD={barcode}")
        String getFoodSafetyInfo(@PathVariable("apiKey") String apiKey, @PathVariable("barcode") String barcode);
}
