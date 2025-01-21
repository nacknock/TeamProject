package com.example.HamuPochi.Service;

import com.example.HamuPochi.Entity.CategoryDetail;
import com.example.HamuPochi.Repository.CategoryDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Autowired
    private CategoryDetailRepository categoryDetailRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String getCategoryFromLabels(List<Map<String, Object>> labels) throws IOException {

        System.out.println("API Key: " + apiKey);
        System.out.println("API URL: " + apiUrl);

        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();


        String prompt = buildPrompt(labels);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "넌 이미지 라벨을 분류하는 AI야"),
                Map.of("role", "user", "content", prompt)
        ));

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }


            Map<String, Object> responseMap = objectMapper.readValue(response.body().string(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        }
    }

    private String buildPrompt(List<Map<String, Object>> labels) {
        List<CategoryDetail> categories = categoryDetailRepository.findAll();

        StringBuilder prompt = new StringBuilder("다음은 이미지 라벨과 그 신뢰도 점수야:\n\n");
        for (Map<String, Object> label : labels) {
            prompt.append("- ").append(label.get("name"))
                    .append(" (confidence: ").append(label.get("confidence")).append(")\n");
        }

        prompt.append("\n다음은 선택 가능한 카테고리 목록이야:\n");
        for (CategoryDetail category : categories) {
            prompt.append("- ").append(category.getCategory_detail_name()).append("\n");
        }

        prompt.append("\n이 라벨들과 카테고리 목록을 기반으로 이 이미지에 가장 적합한 카테고리를 카테고리 목록에서 하나 선택해서 제안해줘 그리고 " +
                "데이터 처리하기 쉽게 카테고리만 출력해줘.");
        return prompt.toString();
    }
}
