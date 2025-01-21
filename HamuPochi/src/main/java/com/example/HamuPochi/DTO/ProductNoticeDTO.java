package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductNoticeDTO {

    private long id;

    private Seller seller_id;

    private Product product_id;

    private String title;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
