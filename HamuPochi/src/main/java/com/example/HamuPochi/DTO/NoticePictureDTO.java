package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticePictureDTO {

    private long id;

    private Notice notice_id;

    private String notice_picture_url;

    private LocalDateTime created_at;

}
