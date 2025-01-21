package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {
    List<ProductDTO> getListWithPaging(Criteria cri);

    long getCountForList(Criteria cri);

    //사용자별 상품 리스트
    List<Product> getProductList(Long id);

    //상품등록
    void setProduct(ProductDTO dto);

    //상품검색
    Optional<Product> existingProduct(Long id);

    //상품수정
    void productUpdate(Product entity);

    //판매자 마이페이지 상품 리스트
    List<ProductDTO> getSellingList(Criteria cri,Long id);
    long getSellingListCount(Criteria cri,Long id);

    Optional<List<Product>> getRecommListByCategory(Product product);

    //관리자 마이페이지 : 판매자가 등록한 상품 전체 리스트
    // 1. 전체 상품 목록 조회
    List<ProductDTO> getAllProductList(Criteria cri);

    // 2. 전체 상품 수 조회
    long getAllProductCount(Criteria cri);

    void updateStatusById(Long id);

    //카테고리 구분 x 랜덤한 상품 리스트 (int i = limit)
    List<Product> findAllNewProductList(int i);

    //카테고리에 속하는 특정 수의 상품 리스트 (int i = limit)
    List<Product> findAsCategoryLimit(long category_id, int i);
}
