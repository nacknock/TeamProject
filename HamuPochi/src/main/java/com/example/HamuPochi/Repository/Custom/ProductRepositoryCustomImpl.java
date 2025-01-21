package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.QProduct;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProduct product = QProduct.product; // 기본 인스턴스 사용

    public ProductRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    //product/list.do getList
    @Override
    public List<ProductDTO> getListWithPaging(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(product.title.contains(cri.getKeyword()));
        }
        if (cri.getCategory() != 0l) {
            builder.and(product.category_id.id.eq(cri.getCategory()));
        }
        List<ProductDTO> list = queryFactory
                .select(Projections.fields(ProductDTO.class,product.id,
                product.title, product.price, product.thumbnail_url))
                .from(product)
                .where(product.status.ne(false).and(builder))
                .orderBy(product.id.desc())
                .offset(cri.getOffset())
                .limit(cri.getAmount())
                .fetch();
        return list;
    }

    @Override
    public long getCountForList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getKeyword() != null) {
            builder.and(product.title.contains(cri.getKeyword()));
        }
        if (cri.getCategory() != 0l) {
            builder.and(product.category_id.id.eq(cri.getCategory()));
        }
        // bno > 0 조건 추가
        builder.and(product.id.gt(0));

        // Count 쿼리 실행
        long count = queryFactory
                .select(product.count())
                .from(product)
                .where(builder.and(product.status.ne(false)))
                .fetchOne();

        return count;
    }

    @Override
    public List<Product> getProductList(Long id) {
        List<Product> list = queryFactory
                .select(product)
                .from(product)
                .where(product.seller_id.id.eq(id).and(product.status.eq(true)))
                .fetch();
        return list;
    }

    @Override
    public void setProduct(ProductDTO dto) {
        queryFactory
            .insert(product)
            .columns(
                    product.seller_id,
                    product.category_id,
                    //product.category_detail_id,
                    product.title,
                    product.content,
                    product.price,
                    product.thumbnail_url,
                    product.status
            )
            .values(
                    dto.getSeller_id().getId(), // Seller 객체에서 ID 추출
                    dto.getCategory_id().getId(), // Category 객체에서 ID 추출
                    //dto.getCategory_detail_id().getId(), // CategoryDetail 객체에서 ID 추출
                    dto.getTitle(),
                    dto.getContent(),
                    dto.getPrice(),
                    dto.getThumbnail_url(),
                    1 // status 값을 직접 입력
            )
            .execute(); // 쿼리 실행

    }

    @Override
    public Optional<Product> existingProduct(Long id) {
        Optional<Product> existingProduct = Optional.ofNullable(queryFactory
                .select(product)
                .from(product)
                .where(product.id.eq(id).and(product.status.eq(true)))
                .fetchOne());
        return existingProduct;
    }

    @Override
    public void productUpdate(Product entity) {
        queryFactory
                .update(product)
                .set(product.title, entity.getTitle())
                .set(product.content, entity.getContent())
                .set(product.price, entity.getPrice())
                .set(product.thumbnail_url, entity.getThumbnail_url())
                .set(product.status, true) // status 값을 직접 입력
                .where(product.id.eq(entity.getId())) // 특정 ID로 필터링
                .execute(); // 쿼리 실행
    }

    @Override
    public List<ProductDTO> getSellingList(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getType() != null && cri.getKeyword() != null) {
            switch (cri.getType()) {
                case "title":
                    builder.and(product.title.contains(cri.getKeyword()));
                    break;
//                case "content":
//                    builder.and(product.content.contains(cri.getKeyword()));
//                    break;
//                case "writer":
//                    builder.and(product.writer.contains(cri.getKeyword()));
//                    break;
            }
        }
        List<ProductDTO> list = queryFactory
                .select(Projections.fields(ProductDTO.class,product.id,
                        product.title, product.price, product.thumbnail_url))
                .from(product)
                .where(product.seller_id.id.eq(id))
                .orderBy(product.id.desc())
                .offset(cri.getOffset())
                .where(product.status.ne(false).and(builder))
                .limit(cri.getAmount())
                .fetch();
        return list;
    }

    @Override
    public long getSellingListCount(Criteria cri, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        // Criteria에 따른 조건 추가
        if (cri.getType() != null && cri.getKeyword() != null) {
            switch (cri.getType()) {
                case "title":
                    builder.and(product.title.contains(cri.getKeyword()));
                    break;
//                case "content":
//                    builder.and(product.content.contains(cri.getKeyword()));
//                    break;
//                case "writer":
//                    builder.and(product.writer.contains(cri.getKeyword()));
//                    break;
            }
        }

        // Count 쿼리 실행
        long count = queryFactory
                .select(product.count())
                .from(product)
                .where(builder.and(product.seller_id.id.eq(id))
                        .and(product.status.ne(false).and(builder))
                )
                .fetchOne();

        return count;
    }

    @Override//동일 카테고리 상품 4개 select
    public Optional<List<Product>> getRecommListByCategory(Product entity) {
        List<Product> list = queryFactory
                .select(product)
                .from(product)
                .where(product.category_id.id.eq(entity.getCategory_id().getId())
                        .and(product.id.ne(entity.getId()))
                        .and(product.status.eq(true))
                )
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc()) // 랜덤 정렬(MySQL)
                .limit(4)
                .fetch();

        return Optional.ofNullable(list);
    }
    // 관리자 페이지 : 판매자 유저들이 등록한 전체 상품 관리 리스트
    // 1. 상품 목록 조회 메서드
    @Override
    public List<ProductDTO> getAllProductList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    product.title.contains(keyword) // 상품 이름 검색
            );
        }

        return queryFactory
                .select(Projections.fields(ProductDTO.class,
                        product.id,
                        product.seller_id,
                        product.category_id,
                        product.title,
                        product.price))
                .from(product)
                .where(builder.and(product.status.eq(true).and(product.status.eq(true)))) // 필터링 조건 추가
                .orderBy(product.created_at.desc()) // 가입일 기준 내림차순 정렬
                .offset(cri.getOffset()) // 페이징을 위해 오프셋 설정
                .limit(cri.getAmount()) // 페이지당 출력할 항목 수 설정
                .fetch(); // 결과 반환
    }

    // 2. 전체 상품 수 조회 메서드
    @Override
    public long getAllProductCount(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    product.title.contains(keyword)
                            .or(product.category_id.category_name.contains(keyword))
                            .or(product.price.stringValue().contains(keyword))
                            .or(product.seller_id.name.contains(keyword))
            );
        }

        return queryFactory
                .select(product.count())
                .from(product)
                .where(builder.and(product.status.eq(true))) // 필터링 조건 추가
                .fetchOne(); // 결과 반환
    }

    @Override
    @Transactional
    public void updateStatusById(Long id) {

        queryFactory.update(product)
                .set(product.status,false)
                .where(product.id.eq(id))
                .execute();
    }

    @Override
    public List<Product> findAllNewProductList(int i) {
        List<Product> list = queryFactory
                .select(product)
                .from(product)
                .where(product.status.eq(true))
                .orderBy(product.created_at.desc())
                .limit(i)
                .fetch();

        return list;
    }

    @Override
    public List<Product> findAsCategoryLimit(long category_id, int i) {
        List<Product> list = queryFactory
                .select(product)
                .from(product)
                .where(product.category_id.id.eq(category_id)
                        .and(product.status.eq(true)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc()) // 랜덤 정렬(MySQL)
                .limit(i)
                .fetch();

        return list;
    }


}
