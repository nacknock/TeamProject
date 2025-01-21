package com.example.HamuPochi.Service;


import com.example.HamuPochi.DTO.LabelDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.rekognition.model.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    private final String accessKeyId;
    private final String secretAccessKey;
    private final String bucketName;
    private final String region;

    private final S3Client s3Client;
    private final RekognitionClient rekognitionClient;

    public RekognitionService(@Value("${aws.accessKeyId}") String accessKeyId,
                              @Value("${aws.secretAccessKey}") String secretAccessKey,
                              @Value("${aws.s3.bucketName}") String bucketName,
                              @Value("${aws.region}") String region) {

        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.bucketName = bucketName;
        this.region = region;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.rekognitionClient = RekognitionClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public Map<String, Object> analyzeImageFromS3(String s3FileKey) {
        Map<String, Object> response = new HashMap<>();

        try {
            DetectLabelsRequest request = DetectLabelsRequest.builder()
                    .image(Image.builder()
                            .s3Object(S3Object.builder()
                                    .bucket(bucketName)
                                    .name(s3FileKey)
                                    .build())
                            .build())
                    .maxLabels(10)
                    .minConfidence(75F)
                    .build();

            DetectLabelsResponse labelsResponse = rekognitionClient.detectLabels(request);

            List<Map<String, Object>> labels = labelsResponse.labels().stream()
                    .map(label -> {
                        Map<String, Object> labelData = new HashMap<>();
                        labelData.put("name", label.name());
                        labelData.put("confidence", label.confidence().doubleValue());
                        return labelData;
                    })
                    .collect(Collectors.toList());

            response.put("labels", labels);
            response.put("message", "Labels detected successfully");

        } catch (RekognitionException e) {
            response.put("error", "Rekognition error: " + e.getMessage());
        }

        return response;
    }
}
