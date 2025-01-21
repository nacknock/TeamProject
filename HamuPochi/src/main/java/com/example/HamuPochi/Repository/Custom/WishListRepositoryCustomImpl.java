package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.DTO.WishListDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.QSeller;
import com.example.HamuPochi.Entity.QWishList;
import com.example.HamuPochi.Entity.WishList;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class WishListRepositoryCustomImpl implements WishListRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QWishList wishList = QWishList.wishList; // 기본 인스턴스 사용

    public WishListRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WishListDTO> getWishList(Long id) {
        List<WishListDTO> list = queryFactory
                .select(Projections.fields(WishListDTO.class,wishList))
                .from(wishList)
                .where(wishList.buyer_id.id.eq(id))
                .fetch();
        return list;

    }

    @Override
    public WishList findByBuyerAndProduct(Buyer buyer, long productId) {
        WishList entity = queryFactory
                .select(wishList)
                .from(wishList)
                .where(wishList.buyer_id.id.eq(buyer.getId())
                        .and(wishList.product_id.id.eq(productId))
                )
                .fetchOne();

        return entity;
    }

    @Override
    public List<WishList> getWishListWithPaging(Criteria cri, long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(wishList.product_id.title.contains(cri.getKeyword()));
        }
        if (cri.getCategory() != 0l) {
            builder.and(wishList.product_id.category_id.id.eq(cri.getCategory()));
        }
        List<WishList> list = queryFactory
                .select(wishList)
                .from(wishList)
                .where(wishList.buyer_id.id.eq(id)
                        .and(builder)
                        .and(wishList.product_id.status.ne(false))
                )
                .orderBy(wishList.id.desc())
                .offset(cri.getOffset())
                .limit(cri.getAmount())
                .fetch();
        return list;
    }

    @Override
    public long getCountForList(Criteria cri, long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(wishList.product_id.title.contains(cri.getKeyword()));
        }
        if (cri.getCategory() != 0l) {
            builder.and(wishList.product_id.category_id.id.eq(cri.getCategory()));
        }
        // bno > 0 조건 추가
        builder.and(wishList.id.gt(0));

        // Count 쿼리 실행
        long count = queryFactory
                .select(wishList.count())
                .from(wishList)
                .where(wishList.buyer_id.id.eq(id)
                        .and(builder)
                        .and(wishList.product_id.status.ne(false)))
                .fetchOne();

        return count;
    }
}
