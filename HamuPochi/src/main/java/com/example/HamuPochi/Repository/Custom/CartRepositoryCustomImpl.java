package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Cart;
import com.example.HamuPochi.Entity.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QCart cart = QCart.cart; // 기본 인스턴스 사용

    public CartRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<List<Cart>> findAllByBuyerId(long id) {

        List<Cart> list = queryFactory
                .select(cart)
                .from(cart)
                .where(cart.buyer_id.id.eq(id))
                .fetch();

        return Optional.ofNullable(list);
    }

    @Override
    public Cart findOneByBuyerAndOption(long buyerId, long id) {

        Cart result = queryFactory
                .select(cart)
                .from(cart)
                .where(cart.buyer_id.id.eq(buyerId)
                        .and(cart.option_id.id.eq(id))
                )
                .fetchOne();

        return result;
    }

    @Override
    public void updateAmount(long id, long amount) {
        queryFactory
                .update(cart)
                .set(cart.amount,amount)
                .where(cart.id.eq(id))
                .execute();
    }
}
