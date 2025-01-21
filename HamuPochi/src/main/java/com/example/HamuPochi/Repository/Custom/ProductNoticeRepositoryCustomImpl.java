package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.ProductNoticeDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Entity.QProductNotice;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class ProductNoticeRepositoryCustomImpl implements ProductNoticeRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProductNotice productNotice = QProductNotice.productNotice; // 기본 인스턴스 사용

    public ProductNoticeRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProductNotice> getMainNoticeList(Long seller_id) {

        List<ProductNotice> mainNoticeList =queryFactory
                .select(productNotice)
                .from(productNotice)
                .where(productNotice.seller_id.id.eq(seller_id))
                .orderBy(productNotice.id.desc())
                .limit(3)
                .fetch();
        return mainNoticeList;
    }

    @Override
    public Optional<List<ProductNotice>> findAllByProduct(Product product) {
        List<ProductNotice> list = queryFactory
                .select(productNotice)
                .from(productNotice)
                .where(productNotice.product_id.id.eq(product.getId()))
                .fetch();

        return Optional.ofNullable(list);
    }
  
    @Override
    public List<ProductNotice> getNoticeList(Criteria cri, Long id) {
        List<ProductNotice> list = queryFactory
                .select(productNotice)
                .from(productNotice)
                .where(productNotice.seller_id.id.eq(id))
                .orderBy(productNotice.id.desc())
                .offset(cri.getOffset())
                .where(productNotice.seller_id.id.eq(id))
                .limit(cri.getAmount())
                .fetch();
        return list;
    }

    @Override
    public long getNoticeCount(Long id) {
        long count = queryFactory
        .select(productNotice.count())
        .from(productNotice)
        .where(productNotice.seller_id.id.eq(id))
        .fetchOne();

        return count;
    }

    @Override
    public void setNoticeUpdate(ProductNotice entity) {
        queryFactory
                .update(productNotice)
                .set(productNotice.title, entity.getTitle())
                .set(productNotice.product_id.id,entity.getProduct_id().getId())
                .set(productNotice.content, entity.getContent())
                .where(productNotice.id.eq(entity.getId())) // 특정 ID로 필터링
                .execute(); // 쿼리 실행
    }


}
