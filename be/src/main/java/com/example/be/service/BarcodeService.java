package com.example.be.service;

import com.example.be.data.entity.Barcode;
import com.example.be.repository.BarcodeRepository;
import com.example.feign.BarcodeApi;
import com.example.feign.eleFood;
import com.example.feign.eleRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeService {

    private final BarcodeRepository barcodeRepository;
    private final BarcodeApi barcodeApi;
    private final eleFood foodApi;
    private final eleRecipe recipeApi;


    public Barcode saveBarcode(Barcode barcode) {
        try {
            barcode.setCreatedDate(LocalDate.now());
            return barcodeRepository.save(barcode);
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 오류를 출력합니다.
            throw new RuntimeException("Failed to save barcode: " + e.getMessage());
        }
    }



    public List<Barcode> listBarcodes(){
        return barcodeRepository.findAll();
    }
    public void deleteByProductName(String productName) {
        barcodeRepository.deleteByProductName(productName);
    }

    public String getDataFromBarcode(String param){
        return barcodeApi.getData(param);
    }
    public String getFoodData(String param){
        return foodApi.getFoodData(param);
    }
    public String getRecipeData(String param){
        return recipeApi.getRecipeData(param);
    }

}

