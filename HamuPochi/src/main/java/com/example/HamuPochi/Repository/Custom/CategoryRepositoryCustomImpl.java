package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.QAnswer;
import com.example.HamuPochi.Entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QCategory category = QCategory.category; // 기본 인스턴스 사용

    public CategoryRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

}
