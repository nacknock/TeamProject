package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.CategoryDetail;
import com.example.HamuPochi.Repository.Custom.CategoryDetailRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {
}