package com.example.be.controller;

import naver.storage.ObjectStorageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true") // CORS 설정 (필요에 따라 조정)
@RequestMapping("/api/food")
public class FileUploadController {

    private final ObjectStorageService objectStorageService;

    public FileUploadController(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }


    @PostMapping("/upload/barcode")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String bucketName = "barcode-reader-finalproject"; // 실제 버킷 이름으로 변경하세요
            String directoryPath = "barcode"; // 실제 경로로 변경하세요

            // 파일을 업로드하고 파일 이름을 반환
            String filename = objectStorageService.uploadFile(bucketName, directoryPath, file);

            // 성공적으로 업로드된 파일의 경로 또는 URL을 반환합니다.
            String fileUrl = directoryPath + "/" + filename;

            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}
