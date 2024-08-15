package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "barcodeApi", url = "https://xnawjp24sv.apigw.ntruss.com")
public interface BarcodeApi {
    @GetMapping("/api/barcode-endpoint")
    String getData(@RequestParam String param);
}
