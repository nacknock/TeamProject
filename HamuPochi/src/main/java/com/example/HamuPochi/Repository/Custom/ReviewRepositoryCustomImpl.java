package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.QReview;
import com.example.HamuPochi.Entity.Review;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QReview review = QReview.review; // 기본 인스턴스 사용

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<List<Review>> findAllByProduct(Product product) {
        List<Review> list = queryFactory
                .select(review)
                .from(review)
                .where(review.product_id.id.eq(product.getId()))
                .fetch();
        return Optional.ofNullable(list);
    }

    @Override
    public Optional<List<Review>> findAllPagingByProduct(Criteria cri, Product product) {
        List<Review> list = queryFactory
                .select(review)
                .from(review)
                .where(review.product_id.id.eq(product.getId()))
                .orderBy(review.id.desc())
                .offset(cri.getOffset())
                .limit(cri.getAmount())
                .fetch();
        return Optional.ofNullable(list);
    }

    @Override
    public long getTotalCount(Criteria cri, Product product) {

        long count = queryFactory
                .select(review.count())
                .from(review)
                .where(review.product_id.id.eq(product.getId()))
                .fetchOne();

        return count;
    }

    @Override
    public List<Double> getRaitByProduct(Product product) {
        List<Double> result = queryFactory
                .select(review.rating)
                .from(review)
                .where(review.product_id.id.eq(product.getId()))
                .fetch();

        return result;
    }
}
