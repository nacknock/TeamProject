package com.example.HamuPochi.DTO;

import com.example.HamuPochi.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDetailDTO {

    private long id;

    private Category category_id;

    private String category_detail_name;

}
