package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Repository.Custom.ProductOptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption,Long> , ProductOptionRepositoryCustom {
}
