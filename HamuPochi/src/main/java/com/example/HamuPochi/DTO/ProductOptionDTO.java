package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductOptionDTO {

    private long id;

    private String option_name;

    private Product product_id;

    private boolean status;

}
