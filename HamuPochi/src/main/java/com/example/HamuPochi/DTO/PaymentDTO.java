package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {

    private long id;

    private String payment_number;

    private String approved_at;

    private long total_payment;

}
