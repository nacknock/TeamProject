package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Category;
import jakarta.persistence.Column;
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
//categoryId 추가 2024/12/29
public class SellerDTO {
    private long id;

    private String email;

    private String password;

    private String name;

    private String company_name;

    private String company_address;

    private String bank_name;

    private long bank_account;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private Category category_id;

}
