package com.example.be.service;

import com.example.feign.BarcodeResultClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BarcodeResultService {

    private final BarcodeResultClient barcodeResultClient;

    @Value("${foodsafety.api.api-key}")
    private String apiKey;

    public BarcodeResultService(BarcodeResultClient barcodeResultClient) {
        this.barcodeResultClient = barcodeResultClient;
    }

    public String getFoodSafetyInfo(String barcode) {
        return barcodeResultClient.getFoodSafetyInfo(apiKey, barcode);
    }

    public String getRecipesByIngredient(String ingredient) {

        return barcodeResultClient.getRecipesByIngredient(apiKey,ingredient);
    }
}
