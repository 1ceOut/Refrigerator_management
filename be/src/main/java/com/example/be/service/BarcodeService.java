package com.example.be.service;

import com.example.be.data.entity.Barcode;
import com.example.be.repository.BarcodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeService {

    @Autowired
    private BarcodeRepository barcodeRepository;

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
}

