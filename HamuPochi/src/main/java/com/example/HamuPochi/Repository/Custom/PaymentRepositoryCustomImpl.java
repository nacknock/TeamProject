package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.QNotice;
import com.example.HamuPochi.Entity.QPayment;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QPayment payment = QPayment.payment; // 기본 인스턴스 사용


    public PaymentRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }





}
