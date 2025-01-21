package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;


import java.util.List;
@Log4j2
public class ProductOrderRepositoryCustomImpl implements ProductOrderRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProductOrder productOrder = QProductOrder.productOrder; // 기본 인스턴스 사용
    private QProductOption productOption = QProductOption.productOption;
    private QProduct product = QProduct.product;
    private QCategory category = QCategory.category;
    private QReview review = QReview.review;
    private QSeller seller = QSeller.seller;
    private QBuyer buyer = QBuyer.buyer;

    public ProductOrderRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProductOrder> getSellerPageOrderList(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(product.title.contains(cri.getKeyword()));
        }
        if (cri.getStatus() != 0) {
            if(cri.getStatus() == 1) {
                builder.and(productOrder.status.eq(true));
            }else{
                builder.and(productOrder.status.eq(false));
            }
        }

        List<ProductOrder> list = queryFactory
                .select(productOrder)
                .from(productOrder)
                .leftJoin(productOption).on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product).on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller).on(seller.id.eq(product.seller_id.id))
                .where(builder.and(seller.id.eq(id)))
                .orderBy(productOrder.id.desc())
                .offset(cri.getOffset())
                .limit(cri.getAmount())
                .fetch();

        return list;
    }

    @Override
    public long getSellerPageOrderCount(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(productOrder.option_id.product_id.title.contains(cri.getKeyword()));
        }
        if (cri.getStatus() != 0) {
            if(cri.getStatus() == 1) {
                builder.and(productOrder.status.eq(true));
            }else{
                builder.and(productOrder.status.eq(false));
            }
        }
        // bno > 0 조건 추가
        builder.and(productOrder.id.gt(0));

        // Count 쿼리 실행

        long count = queryFactory
                .select(productOrder.count())
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller)
                .on(seller.id.eq(product.seller_id.id))
                .where(builder.and(seller.id.eq(id)))
                .fetchOne();

        return count;
    }

    @Override
    public List<Tuple> getBuyerPageOrderList(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getStartDate() != null & cri.getEndDate() == null) {
            builder.and(productOrder.created_at.gt(cri.getStartDate().atStartOfDay()));
        }
        if (cri.getStartDate() == null & cri.getEndDate() != null) {
            builder.and(productOrder.created_at.lt(cri.getEndDate().atStartOfDay()));
        }
        if (cri.getStartDate() != null & cri.getEndDate() != null) {
            builder.and(productOrder.created_at.between(cri.getStartDate().atStartOfDay(), cri.getEndDate().atStartOfDay()));
        }
        if (cri.getKeyword() != null) {
            builder.and(product.title.contains(cri.getKeyword()));
        }

        List<Tuple> list = queryFactory
                .select(productOrder,review)
                .from(productOrder)
                .leftJoin(buyer)
                .on(buyer.id.eq(productOrder.buyer_id.id))
                .leftJoin(review)
                .on(review.buyer_id.eq(productOrder.buyer_id))
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(productOption.product_id.id.eq(product.id))
                .where(builder.and(productOrder.buyer_id.id.eq(id)))
                .orderBy(productOrder.id.desc())
                .offset(cri.getOffset())
                .limit(cri.getAmount())
                .fetch();
        return list;
    }

    @Override
    public long getBuyerPageOrderCount(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getStartDate() != null & cri.getEndDate() == null) {
            builder.and(productOrder.created_at.gt(cri.getStartDate().atStartOfDay()));
        }
        if (cri.getStartDate() == null & cri.getEndDate() != null) {
            builder.and(productOrder.created_at.lt(cri.getEndDate().atStartOfDay()));
        }
        if (cri.getStartDate() != null & cri.getEndDate() != null) {
            builder.and(productOrder.created_at.between(cri.getStartDate().atStartOfDay(), cri.getEndDate().atStartOfDay()));
        }

        // bno > 0 조건 추가
        builder.and(productOrder.id.gt(0));

        // Count 쿼리 실행
        long count = queryFactory
                .select(productOrder.id.count())
                .from(productOrder)
                .where(builder.and(productOrder.buyer_id.id.eq(id)))
                .fetchOne();

        return count;
    }

    @Override
    public List<Tuple> getListForOrderRate() {

        List<Tuple> list = queryFactory
                .select(category.category_name,productOrder.count())
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.id))
                .leftJoin(category)
                .on(category.id.eq(product.category_id.id))
                .groupBy(category.category_name)
                .fetch();

        return list;
    }

    @Override
    public List<Tuple> getTopSellingList(int count) {

        List<Tuple> list = queryFactory
                .select(product,productOrder.count().as("amount"))
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.product_id.id))
                .where(productOrder.status.eq(false))
                .groupBy(product)
                .orderBy(productOrder.count().desc())
                .limit(count)
                .fetch();

        return list;
    }

    @Override
    public ProductOrder findByBuyerAndProduct(long buyer, Product product) {

        ProductOrder result = queryFactory
                .select(productOrder)
                .from(productOrder)
                .where(productOrder.buyer_id.id.eq(buyer)
                        .and(productOrder.option_id.product_id.id.eq(product.getId()))
                    )
                .limit(1)
                .fetchOne();

        return result;
    }

    @Override
    public void updateStatusToTrue(Long id) {
        queryFactory
                .update(productOrder)
                .set(productOrder.status,true)
                .where(productOrder.id.eq(id))
                .execute();
    }

    @Override
    public List<Tuple> getListForOrderRateForSeller(Long id) {
        List<Tuple> list = queryFactory
                .select(product.title,productOrder.count())
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller)
                .on(seller.id.eq(product.seller_id.id))
                .where(seller.id.eq(id))
                .groupBy(product.title)
                .fetch();

        return list;
    }

    @Override
    public long countForSeller(long id) {
        long count = queryFactory
                .select(productOrder.count())
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller)
                .on(seller.id.eq(product.seller_id.id))
                .where(seller.id.eq(id))
                .fetchOne();
        return count;
    }

    @Override
    public Product findBestSeller(Long id) {
        Product entity = queryFactory
                .select(product)
                .from(productOrder)
                .leftJoin(productOption)
                .on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product)
                .on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller)
                .on(seller.id.eq(product.seller_id.id))
                .where(seller.id.eq(id))
                .groupBy(product)
                .orderBy(productOrder.count().desc())
                .limit(1)
                .fetchOne();

        return entity;

    }

    @Override
    public List<ProductOrder> getSellerMainPageOrderList(Long id) {
        List<ProductOrder> list = queryFactory
                .select(productOrder)
                .from(productOrder)
                .leftJoin(productOption).on(productOption.id.eq(productOrder.option_id.id))
                .leftJoin(product).on(product.id.eq(productOption.product_id.id))
                .leftJoin(seller).on(seller.id.eq(product.seller_id.id))
                .where(seller.id.eq(id))
                .fetch();

        return list;
    }
}
