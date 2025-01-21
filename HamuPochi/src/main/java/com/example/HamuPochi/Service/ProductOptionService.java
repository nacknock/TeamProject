package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductOptionDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductOptionService {

    default ProductOption DtoToEntity(ProductOptionDTO dto){
        ProductOption entity =ProductOption.builder()
                .id(dto.getId())
                .option_name(dto.getOption_name())
                .product_id(dto.getProduct_id())
                .status(dto.isStatus())
                .build();

        return entity;
    }

    default ProductOptionDTO EntityToDTO(ProductOption entity){
        ProductOptionDTO dto = ProductOptionDTO.builder()
                .id(entity.getId())
                .option_name(entity.getOption_name())
                .product_id(entity.getProduct_id())
                .status(entity.isStatus())
                .build();

        return dto;
    }

    ProductOption save(String optionName, Product entity);

    Optional<List<ProductOption>> findAllByProduct(Product product);

    ProductOption findById(long optionId);

    void optionUpdate(List<String> optionList, long id);

    void optionUpdateStatusToFalseAllByProduct(long id);

    List<String> checkUpTag(List<String> optionList,long id);

    void optionUpFalseByUpdate(List<String> optionList,List<String> findNameAll,long id);

}
