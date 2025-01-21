package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Repository.Custom.ProductNoticeRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductNoticeRepository extends JpaRepository<ProductNotice,Long> , ProductNoticeRepositoryCustom {
}
