package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.QCategoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CategoryDetailRepositoryCustomImpl implements CategoryDetailRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QCategoryDetail categoryDetail = QCategoryDetail.categoryDetail; // 기본 인스턴스 사용

    public CategoryDetailRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

}
