package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepositoryCustom {
    Optional<List<ProductOption>> findAllByProduct(Product product);

    void updateStatusToFalseAllByProduct(long id);

    List<String> nameFindAllByProductId(long id);

    List<String> findAllNameByPId(long id);

    void UpdateFalseOneByPId(String optionName, long id);
}
