package com.example.be.service;

import com.example.be.data.entity.Barcode;
import com.example.be.repository.BarcodeRepository;
import com.example.be.repository.RefrigeRatorRepository;
import com.example.feign.OcrClient;
import com.example.feign.eleFood;
import com.example.feign.eleRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeService {

    private final BarcodeRepository barcodeRepository;
    private final RefrigeRatorRepository refrigeRatorRepository;
    private final OcrClient ocrClient;
    private final eleFood foodApi;
    private final eleRecipe recipeApi;
    private final FoodService foodService;


//    public Barcode saveBarcode(Barcode barcode) {
//        try {
//            // refrigerator_name이 이미 존재하는지 확인합니다.
//            Barcode existingBarcode = barcodeRepository.findByRefrigeratorName(barcode.getRefrigeratorName());
//
//            System.out.println("refrigeratorname : ????????????????"+barcode.getRefrigeratorName());
//            System.out.println("refrigeratorName: !!!!!!!!!!!!!!!!!!!"+existingBarcode);
//
//            if (existingBarcode != null) {
//                // 중복된 경우, count를 증가시킵니다.
//                existingBarcode.setCount(existingBarcode.getCount() + 1);
//                return barcodeRepository.save(existingBarcode);
//            } else {
//                // 중복되지 않는 경우, 새로운 바코드를 생성합니다.
//                barcode.setCreatedDate(LocalDate.now());
//                return barcodeRepository.save(barcode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // 로그에 오류를 출력합니다.
//            throw new RuntimeException("Failed to save barcode: " + e.getMessage());
//        }
//    }
//public Barcode saveBarcode(Barcode barcode) {
//    try {
//        Barcode existingBarcode = barcodeRepository.findByRefrigeratorNameAndProductName(
//                barcode.getRefrigeratorName(), barcode.getProductName());
//
//        if (existingBarcode != null) {
//            // 기존 데이터가 있는 경우
//            if (existingBarcode.getProductName().equals(barcode.getProductName())) {
//                // 동일한 productName이 있는 경우, count와 createdDate만 업데이트
//                existingBarcode.setCount(Integer.toString(Integer.parseInt(existingBarcode.getCount()) + Integer.parseInt(barcode.getCount())));
//                existingBarcode.setCreatedDate(LocalDate.now());
//                return barcodeRepository.save(existingBarcode);
//            } else {
//                // productName이 다르면 모든 값을 업데이트
//                barcode.setCreatedDate(LocalDate.now());
//                return barcodeRepository.save(barcode);
//            }
//        } else {
//            // 기존 데이터가 없는 경우
//            barcode.setCreatedDate(LocalDate.now());
//            return barcodeRepository.save(barcode);
//        }
//    } catch (Exception e) {
//        e.printStackTrace(); // 로그에 오류를 출력합니다.
//        throw new RuntimeException("Failed to save barcode: " + e.getMessage());
//    }
//}


//public Barcode saveBarcode(Barcode barcode) {
//    try {
//        barcode.setCreatedDate(LocalDate.now());
//        return barcodeRepository.save(barcode);
//    } catch (Exception e) {
//        e.printStackTrace(); // 로그에 오류를 출력합니다.
//        throw new RuntimeException("Failed to save barcode: " + e.getMessage());
//    }
//}


    @Transactional
    public Barcode saveBarcode(Barcode barcode) {
        // Save the barcode
        barcode.setCreatedDate(LocalDate.now());
        Barcode savedBarcode = barcodeRepository.save(barcode);

        // Retrieve the product name and refrigerator name
        String productName = barcode.getProductName();
        String refrigeratorName = barcode.getRefrigeratorName(); // Ensure the Barcode object has this method

        System.out.println("productName===================" + productName);
        System.out.println("refrigeratorName===================" + refrigeratorName);

        // Create a relationship with the specified RefrigeRator
        barcodeRepository.createRelationship(productName, refrigeratorName);

        return savedBarcode;
    }



    public List<Barcode> listBarcodesByRefrigeratorName(String refrigeratorName) {

        System.out.println("냉장고이름 ?: "+refrigeratorName);
        return barcodeRepository.findFoodsByRefrigeratorName(refrigeratorName);
    }

    public List<Barcode> listBarcodes(){
        return barcodeRepository.findAll();
    }
    public void deleteByProductName(String productName) {
        barcodeRepository.deleteByProductName(productName);
    }



    public String getFoodData(String param){
        return foodApi.getFoodData(param);
    }
    public String getRecipeData(String param){
        return recipeApi.getRecipeData(param);
    }

}

