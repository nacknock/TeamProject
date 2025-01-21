package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductNoticeDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductNoticeService {

    default ProductNotice DtoToEntity(ProductNoticeDTO dto){
        ProductNotice entity =ProductNotice.builder()
                .id(dto.getId())
                .seller_id(dto.getSeller_id())
                .product_id(dto.getProduct_id())
                .title(dto.getTitle())
                .content(dto.getContent())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default ProductNoticeDTO EntityToDTO(ProductNotice entity){
        ProductNoticeDTO dto = ProductNoticeDTO.builder()
                .id(entity.getId())
                .seller_id(entity.getSeller_id())
                .product_id(entity.getProduct_id())
                .title(entity.getTitle())
                .content(entity.getContent())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    List<ProductNotice> getMainNoticeList(Long seller_id);

    Optional<List<ProductNotice>> findAllByProduct(Product product);

    List<ProductNotice> getNoticeList(Criteria cri, Long id);

    long getNoticeCount(Long id);

    void setNotice(ProductNoticeDTO dto);

    Optional<ProductNotice> findById(Long id);

    void setNoticeUpdate(ProductNoticeDTO dto);

    void noticeDelete(Long id);
}
