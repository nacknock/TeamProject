package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    default Product DtoToEntity(ProductDTO dto){
        Product entity =Product.builder()
                .id(dto.getId())
                .seller_id(dto.getSeller_id())
                .category_id(dto.getCategory_id())
                .category_detail_id(dto.getCategory_detail_id())
                .title(dto.getTitle())
                .content(dto.getContent())
                .price(dto.getPrice())
                .thumbnail_url(dto.getThumbnail_url())
                .status(dto.isStatus())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .amount(dto.getAmount())
                .build();

        return entity;
    }

    default ProductDTO EntityToDTO(Product entity){
        ProductDTO dto = ProductDTO.builder()
                .id(entity.getId())
                .seller_id(entity.getSeller_id())
                .category_id(entity.getCategory_id())
                .category_detail_id(entity.getCategory_detail_id())
                .title(entity.getTitle())
                .content(entity.getContent())
                .price(entity.getPrice())
                .thumbnail_url(entity.getThumbnail_url())
                .status(entity.isStatus())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .amount(entity.getAmount())
                .build();

        return dto;
    }

    List<ProductDTO> getListWithPaging(Criteria cri);

    long getTotalCount(Criteria cri);

    Product setProduct(ProductDTO dto);

    Optional<Product> existingProduct(Long id);

    List<ProductDTO> getSellingList(Criteria cri,Long id);

    long getSellingListCount(Criteria cri,Long id);

    Product findOne(long id);

    List<Product> getProductList(Long id);
  
    Optional<List<Product>> getRecommList(Product product);

    long totalCount();
  
    void productUpdate(ProductDTO dto);

    // 관리자 페이지 : 상품 list
    List<ProductDTO> getProductList(Criteria cri);
    long getProductCount(Criteria cri);

    Optional<Product> readProduct(Long id);

    @Transactional
    void updateStatusById(Long id);

    List<Product> findAllNewProductList(int i);

    List<Product> findAsCategoryLimit(long category_id, int i);

    void deleteOneById(long id);
}
