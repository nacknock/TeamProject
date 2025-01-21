package com.example.HamuPochi.DTO;

public class LabelDTO {

    private String name;
    private Double confidence;

    public LabelDTO(String name, Double confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
