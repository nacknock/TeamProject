package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BanDTO;
import com.example.HamuPochi.DTO.CategoryDTO;
import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    default Category DtoToEntity(CategoryDTO dto){
        Category entity =Category.builder()
                .id(dto.getId())
                .category_name(dto.getCategory_name())
                .build();

        return entity;
    }

    default CategoryDTO EntityToDTO(Category entity){
        CategoryDTO dto = CategoryDTO.builder()
                .id(entity.getId())
                .category_name(entity.getCategory_name())
                .build();

        return dto;
    }
    //select * from category
    List<Category> findAll();
}
