package com.example.be.controller;

import com.example.be.service.BarcodeResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class BarcodeResultController {

    private final BarcodeResultService barcodeResultService;

    @GetMapping("/barcode/result")
    public String getFoodSafetyInfo(@RequestParam String barcode) {
        System.out.println(barcode);
        return barcodeResultService.getFoodSafetyInfo(barcode);
    }
}
