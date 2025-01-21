package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.QBan;
import com.example.HamuPochi.Entity.Seller;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class BanRepositoryCustomImpl implements BanRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QBan ban = QBan.ban; // 기본 인스턴스 사용

    public BanRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long countByBuyer(Buyer buyer) {

        long banCount = queryFactory
                .select(ban.count())
                .from(ban)
                .where(ban.buyer_id.id.eq(buyer.getId()))
                .fetchOne();

        return banCount;
    }

    @Override
    public long countBySeller(Seller seller) {
        long banCount = queryFactory
                .select(ban.count())
                .from(ban)
                .where(ban.seller_id.id.eq(seller.getId()))
                .fetchOne();

        return banCount;
    }

    @Override
    public Ban findLastByBuyerId(long id) {
        Ban entity = queryFactory
                .select(ban)
                .from(ban)
                .where(ban.buyer_id.id.eq(id).and(ban.status.eq(true)))
                .orderBy(ban.created_at.desc())
                .fetchOne();
        return entity;
    }

    @Override
    public Ban findLastBySellerId(long id) {
        Ban entity = queryFactory
                .select(ban)
                .from(ban)
                .where(ban.seller_id.id.eq(id).and(ban.status.eq(true)))
                .orderBy(ban.created_at.desc())
                .fetchOne();
        return entity;
    }

    @Override
    public void banOff(Ban entity) {
        queryFactory
                .update(ban)
                .set(ban.status,false)
                .execute();
    }

    @Override
    public Ban findLastByBuyerEmail(String email) {
        Ban entity = queryFactory
                .select(ban)
                .from(ban)
                .where(ban.buyer_id.email.eq(email).and(ban.status.eq(true)))
                .fetchOne();

        return entity;
    }

    @Override
    public Ban findLastBySellerEmail(String email) {
        Ban entity = queryFactory
                .select(ban)
                .from(ban)
                .where(ban.seller_id.email.eq(email).and(ban.status.eq(true)))
                .fetchOne();

        return entity;
    }
}
