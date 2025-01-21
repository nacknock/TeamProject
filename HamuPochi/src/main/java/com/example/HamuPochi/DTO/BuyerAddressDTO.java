package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Buyer;
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
public class BuyerAddressDTO {

    private long id;

    private Buyer buyer_id;

    private String prefecture; //都道府県

    private String city;//市区町村

    private String block_number; //丁目・番地・号

    private String building_name; //建物名／会社名・部屋番号

    private String post_number;

    private boolean status;

    private LocalDateTime created_at;

    private String receiver;

    private String tel;

}
