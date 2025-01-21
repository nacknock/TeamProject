package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;
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
public class CartDTO {

    private long id;

    private ProductOption option_id;

    private Buyer buyer_id;

    private long amount;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
