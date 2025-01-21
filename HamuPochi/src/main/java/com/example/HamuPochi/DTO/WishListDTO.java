package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishListDTO {

    private long id;

    private Product product_id;

    private Buyer buyer_id;

    private LocalDateTime created_at;

}
