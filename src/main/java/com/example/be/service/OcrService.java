package com.example.be.service;

import com.example.be.Request.OcrRequest;
import com.example.feign.OcrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OcrService {

    @Value("${ncp.ocr.secret.key}")
    private String secretKey;

    private final OcrClient ocrClient;

    public OcrService(OcrClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    public String recognizeText(OcrRequest request) {
        return ocrClient.recognizeText(secretKey, request).getBody();
    }
}
