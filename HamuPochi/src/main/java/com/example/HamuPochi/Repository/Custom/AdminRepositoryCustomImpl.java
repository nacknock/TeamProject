package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Admin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.HamuPochi.Entity.QAdmin;

import java.util.Optional;

public class AdminRepositoryCustomImpl implements AdminRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QAdmin admin = QAdmin.admin1; // 기본 인스턴스 사용

    public AdminRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Admin> findByIdCustom(String username) {

        Admin result = queryFactory
                .select(admin)
                .from(admin)
                .where(admin.admin.eq(username))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
