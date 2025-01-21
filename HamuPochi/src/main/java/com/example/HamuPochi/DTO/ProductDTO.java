package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.*;
import jakarta.persistence.*;
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
public class ProductDTO {

    private long id;

    private Seller seller_id;

    private Category category_id;

    private CategoryDetail category_detail_id;

    private String title;

    private String content;

    private long price;

    private String thumbnail_url;

    private boolean status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private long amount;

}
