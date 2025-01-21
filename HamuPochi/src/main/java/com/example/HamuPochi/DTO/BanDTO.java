package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
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
public class BanDTO {

    private long id;

    private Seller seller_id;

    private Buyer buyer_id;

    private String reason;

    private boolean status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;


}
