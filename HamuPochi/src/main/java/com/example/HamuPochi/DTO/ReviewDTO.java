package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.*;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {

    private long id;

    private Buyer buyer_id;

    private Product product_id;

    private String content;

    private double rating;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
