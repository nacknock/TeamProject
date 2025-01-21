package com.example.HamuPochi.Controller;


import com.example.HamuPochi.Service.RekognitionService;
import com.example.HamuPochi.Service.S3Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/categorize")
public class RekognitionController {

    private final RekognitionService rekognitionService;
    private final S3Service s3Service;

    public RekognitionController(RekognitionService rekognitionService, S3Service s3Service) {
        this.rekognitionService = rekognitionService;
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();
        String directory = "uploaded-images";

        try {
            // S3에 파일 업로드
            String s3FileKey = s3Service.uploadFile(image, directory);

            // Rekognition 분석 실행
            response = rekognitionService.analyzeImageFromS3(s3FileKey);
            response.put("message", "Image uploaded and analyzed successfully");
            response.put("fileUrl", s3Service.getFileUrl(s3FileKey));

        } catch (IOException e) {
            response.put("error", "Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            response.put("error", "Unexpected error: " + e.getMessage());
        }

        return response;
    }
}