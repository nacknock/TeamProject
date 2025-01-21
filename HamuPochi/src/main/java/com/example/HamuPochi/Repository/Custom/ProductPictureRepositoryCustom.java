package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductPicture;

import java.util.List;
import java.util.Optional;

public interface ProductPictureRepositoryCustom {

    void SetProductFiles(Product entity, List<String> filePaths);

    Optional<List<ProductPicture>> findAllByProduct(Product product);

    void filesDelete(Long id);
}
