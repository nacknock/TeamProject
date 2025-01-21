package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductPicture;
import com.example.HamuPochi.Repository.Custom.ProductPictureRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture,Long> , ProductPictureRepositoryCustom {
}
