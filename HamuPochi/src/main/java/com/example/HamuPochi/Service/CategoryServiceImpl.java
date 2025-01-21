package com.example.HamuPochi.Service;

import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Repository.BuyerRepository;
import com.example.HamuPochi.Repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override//select * from category
    public List<Category> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list;
    }
}
