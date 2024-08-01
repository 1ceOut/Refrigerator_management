package com.example.be.cotroller;

import com.example.be.data.entity.Barcode;
import com.example.be.service.BarcodeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 (필요에 따라 조정)
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarcodeController {

    private final BarcodeService barcodeService;
    private static final Logger logger = LoggerFactory.getLogger(BarcodeController.class); // 로거 설정


    //상품 추가
    @PostMapping("/barcodes")
    public ResponseEntity<Barcode> saveBarcode(@RequestBody @Validated Barcode barcode) {
        try {
            Barcode savedBarcode = barcodeService.saveBarcode(barcode);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBarcode); // 저장 성공 시 201 Created 응답
        } catch (Exception e) {
            logger.error("Error occurred while saving barcode", e); // 오류를 로깅합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 응답 본문을 null로 설정
        }
    }

    //상품 조회
    @GetMapping("/list")
    public List<Barcode> listBarcodes(){
        return barcodeService.listBarcodes();
    }


    //상품 삭제
    @DeleteMapping("/barcodes/{productName}")
    public ResponseEntity<Void> deleteByProductName(@PathVariable String productName) {
        barcodeService.deleteByProductName(productName);
        return ResponseEntity.noContent().build(); // HTTP 204 상태 코드
    }
}
