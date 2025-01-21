package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.Review;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom {
    Optional<List<Review>> findAllByProduct(Product product);

    Optional<List<Review>> findAllPagingByProduct(Criteria cri, Product product);

    long getTotalCount(Criteria cri, Product product);

    List<Double> getRaitByProduct(Product product);
}
