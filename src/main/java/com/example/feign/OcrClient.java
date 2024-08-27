package com.example.feign;

import com.example.be.Request.OcrRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;


@FeignClient(name = "ocrClient", url = "${ncp.ocr.api.url}")
public interface OcrClient {

    @PostMapping
    ResponseEntity<String> recognizeText(
            @RequestHeader("X-OCR-SECRET") String secretKey,
            @RequestBody OcrRequest request
    );
}
