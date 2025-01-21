package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.CategoryDTO;
import com.example.HamuPochi.DTO.CategoryDetailDTO;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.CategoryDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryDetailService {

    default CategoryDetail DtoToEntity(CategoryDetailDTO dto){
        CategoryDetail entity =CategoryDetail.builder()
                .id(dto.getId())
                .category_id(dto.getCategory_id())
                .category_detail_name(dto.getCategory_detail_name())
                .build();

        return entity;
    }

    default CategoryDetailDTO EntityToDTO(CategoryDetail entity){
        CategoryDetailDTO dto = CategoryDetailDTO.builder()
                .id(entity.getId())
                .category_id(entity.getCategory_id())
                .category_detail_name(entity.getCategory_detail_name())
                .build();

        return dto;
    }

    List<CategoryDetailDTO> getAllCategoryDetails();
}
