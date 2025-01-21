package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductPictureDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductPicture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductPictureService {

    default ProductPicture DtoToEntity(ProductPictureDTO dto){
        ProductPicture entity =ProductPicture.builder()
                .id(dto.getId())
                .product_id(dto.getProduct_id())
                .product_picture_url(dto.getProduct_picture_url())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default ProductPictureDTO EntityToDTO(ProductPicture entity){
        ProductPictureDTO dto = ProductPictureDTO.builder()
                .id(entity.getId())
                .product_id(entity.getProduct_id())
                .product_picture_url(entity.getProduct_picture_url())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    void SetProductFiles(Product entity, List<String> filePaths);

    Optional<List<ProductPicture>> findAllByProduct(Product product);

    void filesDelete(Long id);

}
