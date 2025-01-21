package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Question;
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
public class AnswerDTO {

    private long id;

    private Question question_id;

    private Admin admin_id;

    private Seller seller_id;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
