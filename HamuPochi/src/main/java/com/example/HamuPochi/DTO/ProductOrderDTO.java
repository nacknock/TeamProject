package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductOrderDTO {

    private long id;

    private ProductOption option_id;

    private Buyer buyer_id;

    private Payment payment_id;

    private String receiver;

    private long amount;

    private String address;

    private String phone_number;

    private boolean status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private Review review;


}
