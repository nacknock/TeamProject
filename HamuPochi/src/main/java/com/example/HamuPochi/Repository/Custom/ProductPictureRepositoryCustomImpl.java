package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductPicture;
import com.example.HamuPochi.Entity.QProductPicture;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class ProductPictureRepositoryCustomImpl implements ProductPictureRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProductPicture productPicture = QProductPicture.productPicture; // 기본 인스턴스 사용

    public ProductPictureRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void SetProductFiles(Product entity, List<String> filePaths) {
        for (String filePath : filePaths) {
            queryFactory
                    .insert(productPicture)
                    .columns(
                            productPicture.product_id,
                            productPicture.product_picture_url
                    )
                    .values(
                            entity.getId(), // 상품 ID
                            filePath
                    )
                    .execute(); // 쿼리 실행
        }
    }

    @Override
    public Optional<List<ProductPicture>> findAllByProduct(Product product) {

        List<ProductPicture> list = queryFactory
                .select(productPicture)
                .from(productPicture)
                .where(productPicture.product_id.id.eq(product.getId()))
                .fetch();

        return Optional.ofNullable(list);
    }

    @Override
    public void filesDelete(Long id) {
        queryFactory
                .delete(productPicture)
                .where(productPicture.product_id.id.eq(id))
                .execute();
    }
}