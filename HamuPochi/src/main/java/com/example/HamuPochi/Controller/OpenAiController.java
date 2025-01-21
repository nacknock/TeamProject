package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorize")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/ask")
    public Map<String, Object> categorizeImage(@RequestBody Map<String, Object> rekognitionResult) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> labels = (List<Map<String, Object>>) rekognitionResult.get("labels");
            String category = openAiService.getCategoryFromLabels(labels);
            response.put("category", category);
        } catch (Exception e) {
            response.put("error", "Failed to categorize image: " + e.getMessage());
        }
        return response;
    }
}