package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Admin;
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
public class NoticeDTO {

    private long id;

    private Admin admin_id;

    private String title;

    private String content;

    private String notice_picture_url;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
