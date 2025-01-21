package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.ProductNoticeDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;
import java.util.Optional;

public interface ProductNoticeRepositoryCustom {

    List<ProductNotice> getMainNoticeList(Long seller_id);

    Optional<List<ProductNotice>> findAllByProduct(Product product);

    List<ProductNotice> getNoticeList(Criteria cri, Long id);

    long getNoticeCount(Long id);

    void setNoticeUpdate(ProductNotice entity);
}
