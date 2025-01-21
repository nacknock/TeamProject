package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Repository.Custom.BanRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> , CategoryRepositoryCustom {
}
