package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Answer;
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
//@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionDTO {

    private long id;

    private Admin admin_id;

    private Buyer buyer_id;

    private Seller seller_id;

    private String title;

    private String content;

    private boolean status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private String category;

    private Answer answer;

    // 추가: Answer가 null인 경우를 처리할 수 있는 생성자
    public QuestionDTO(long id, Admin admin_id, Buyer buyer_id, Seller seller_id, String title,
                       String content, boolean status, LocalDateTime created_at, LocalDateTime updated_at,
                       String category, Answer answer) {
        this.id = id;
        this.admin_id = admin_id;
        this.buyer_id = buyer_id;
        this.seller_id = seller_id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.category = category;
        this.answer = answer;
    }
}
