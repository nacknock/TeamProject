package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.ProductOrder;
import com.example.HamuPochi.Repository.Custom.ProductOptionRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.ProductOrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder,Long> , ProductOrderRepositoryCustom {
}
