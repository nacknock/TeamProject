package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.Product;
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
public class ProductPictureDTO {

    private long id;

    private Product product_id;

    private String product_picture_url;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
