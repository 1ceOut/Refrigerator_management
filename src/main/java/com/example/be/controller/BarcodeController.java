package com.example.be.controller;

import com.example.be.Request.OcrRequest;
import com.example.be.data.entity.Barcode;
import com.example.be.service.BarcodeService;
import com.example.be.service.FoodService;
import com.example.be.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // CORS 설정 (필요에 따라 조정)
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarcodeController {

    private final BarcodeService barcodeService;
    private final OcrService ocrService;
    private final FoodService foodService;

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^https?:\\/\\/.+"
    );

    // 상품 추가
    @PostMapping("/barcodes")
    public ResponseEntity<Barcode> saveBarcode(@RequestBody @Validated Barcode barcode) {
        Barcode savedBarcode = barcodeService.saveBarcode(barcode);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBarcode);
    }


    // 상품 조회
    @GetMapping("/list")
    public List<Barcode> listBarcodes(@RequestParam(required = false) String refrigeratorName) {
        System.out.println("refriname-==="+refrigeratorName);
        if (refrigeratorName != null) {
            return barcodeService.listBarcodesByRefrigeratorName(refrigeratorName);
        } else {
            return barcodeService.listBarcodes();
        }
    }
    //전체 상품 조회
    @GetMapping("/list/all")
    public List<Barcode> listBarcodes() {
        return barcodeService.listBarcodes();
    }

    // 상품 삭제
    @DeleteMapping("/barcodes/{productName}")
    public ResponseEntity<Void> deleteByProductName(@PathVariable String productName) {
        barcodeService.deleteByProductName(productName);
        return ResponseEntity.noContent().build(); // HTTP 204 상태 코드
    }

    @PostMapping("/ocr")
    public ResponseEntity<String> recognizeText(@RequestBody OcrRequest request) {
        // 요청 데이터 유효성 검사
        if (request.getImages() == null || request.getImages().length == 0) {
            return ResponseEntity.badRequest().body("Image data is missing.");
        }

        for (OcrRequest.Image image : request.getImages()) {
            if (image.getData() == null && image.getUrl() == null) {
                return ResponseEntity.badRequest().body("Each image must have either a data or url field.");
            }
            if (image.getUrl() != null && !URL_PATTERN.matcher(image.getUrl()).matches()) {
                return ResponseEntity.badRequest().body("Invalid URL format: " + image.getUrl());
            }
        }

        try {
            String response = ocrService.recognizeText(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }

    @GetMapping("/foodapi")
    public String getFoodData(@RequestParam("param") String param) {
        return barcodeService.getFoodData("mcategory:" + param);
    }
}
