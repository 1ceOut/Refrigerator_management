package com.example.be.Request;

import lombok.Data;

@Data
public class OcrRequest {
    private Image[] images;
    private String requestId;
    private String version;
    private long timestamp;

    @Data
    public static class Image {
        private String format;
        private String name;
        private String data; // Base64 인코딩된 이미지 데이터
        private String url; // 이미지 URL
    }
}
