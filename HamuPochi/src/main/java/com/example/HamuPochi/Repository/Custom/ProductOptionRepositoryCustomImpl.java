package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Entity.QProductOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class ProductOptionRepositoryCustomImpl implements ProductOptionRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProductOption productOption = QProductOption.productOption; // 기본 인스턴스 사용

    public ProductOptionRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override//select * from product_option where product_id = ?
    public Optional<List<ProductOption>> findAllByProduct(Product product) {

        List<ProductOption> list = queryFactory
                .select(productOption)
                .from(productOption)
                .where(productOption.product_id.id.eq(product.getId())
                        .and(productOption.status.eq(true))
                )
                .fetch();

        return Optional.ofNullable(list);
    }

    @Override
    public void updateStatusToFalseAllByProduct(long id) {

        queryFactory
                .update(productOption)
                .set(productOption.status,false)
                .where(productOption.product_id.id.eq(id))
                .execute();
    }

    @Override
    public List<String> nameFindAllByProductId(long id) {

        List<String> result = queryFactory
                .select(productOption.option_name)
                .from(productOption)
                .where(productOption.product_id.id.eq(id))
                .fetch();
        return result;
    }

    @Override
    public List<String> findAllNameByPId(long id) {
        List<String> result = queryFactory
                .select(productOption.option_name)
                .from(productOption)
                .where(productOption.product_id.id.eq(id))
                .fetch();

        return result;
    }

    @Override
    public void UpdateFalseOneByPId(String optionName, long id) {
        queryFactory
                .update(productOption)
                .set(productOption.status,false)
                .where(productOption.product_id.id.eq(id)
                        .and(productOption.option_name.eq(optionName))
                )
                .execute();
    }
}
