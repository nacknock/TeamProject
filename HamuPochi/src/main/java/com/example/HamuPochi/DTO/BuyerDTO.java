package com.example.HamuPochi.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
//DTO는 파일명 테이블명+DTO
//Vo처럼 만들면 됩니다
public class BuyerDTO {
    private long id;

    private String email;

    private String password;

    private String nickname;

    private String name;

    private String phone_number;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
