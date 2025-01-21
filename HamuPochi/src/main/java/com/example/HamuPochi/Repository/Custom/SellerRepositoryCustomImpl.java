package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.QReview;
import com.example.HamuPochi.Entity.QSeller;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class SellerRepositoryCustomImpl implements SellerRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QSeller seller = QSeller.seller; // 기본 인스턴스 사용

    public SellerRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Seller> findByIdCustom(String username) {

        Seller result = queryFactory
                .select(seller)
                .from(seller)
                .where(seller.email.eq(username))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> findByCompanyName(Long id) {

        Optional<String> company_name = Optional.ofNullable(queryFactory
                .select(seller.company_name)
                .from(seller)
                .where(seller.id.eq(id))
                .fetchOne());

        return Optional.ofNullable(company_name).orElse(null);
    }

    @Override
    public void sellerUpdate(Seller entity) {
        queryFactory
                .update(seller)
                .set(seller.company_name,entity.getCompany_name())
                .set(seller.password,entity.getPassword())
                .set(seller.name,entity.getName())
                .set(seller.company_address,entity.getCompany_address())
                .where(seller.id.eq(entity.getId()))
                .execute();
    }

    @Override
    public Seller findByDTO(SellerDTO sellerDTO) {
        Seller result =queryFactory
                .select(seller)
                .from(seller)
                .where(seller.name.eq(sellerDTO.getName())
                        .and(seller.company_name.eq(sellerDTO.getCompany_name()))
                        .and(seller.company_address.eq(sellerDTO.getCompany_address()))
                )
                .fetchOne();

        return result;
    }

    @Override
    public Seller findByIdPw(String email, String encodePwd) {
        Seller result = queryFactory
                .select(seller)
                .from(seller)
                .where(seller.email.eq(email)
                        .and(seller.password.eq(encodePwd)))
                .fetchOne();

        return result;
    }

    @Override
    public void UpdatePwByEntity(Seller entity, String encodePwd) {
        queryFactory
                .update(seller)
                .set(seller.password,encodePwd)
                .where(seller.id.eq(entity.getId()))
                .execute();
    }
          
    public void withdrawal(Long id) {
        queryFactory
                .delete(seller)
                .where(seller.id.eq(id))
                .execute();
    }

    //관리자 페이지 : 유저 정보 목록 조회 기능를 위한 리포지터리들
    //1. 판매자 유저 정보 목록 조회
    @Override
    public List<SellerDTO> getSellerUserList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    seller.name.contains(keyword) // 이름 검색
                            .or(seller.email.contains(keyword)) // 이메일 검색
                            .or(seller.company_name.contains(keyword)) // 닉네임 검색
                            .or(seller.created_at.stringValue().contains(keyword)) // 가입일 숫자 검색
            );
        }

        return queryFactory
                .select(Projections.fields(SellerDTO.class,
                        seller.id,
                        seller.email,
                        seller.company_name,
                        seller.name,
                        seller.created_at)) // 가입일 포함
                .from(seller) // seller 엔티티에서 데이터 가져오기
                .where(builder) // 필터링 조건 추가
                .orderBy(seller.created_at.desc()) // 가입일 기준 내림차순 정렬
                .offset(cri.getOffset()) // 페이징을 위해 오프셋 설정
                .limit(cri.getAmount()) // 페이지당 출력할 항목 수 설정
                .fetch(); // 결과 반환
    }

    // 2. 전체 판매자 유저 수
    @Override
    public long countSellers(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    seller.name.contains(keyword)
                            .or(seller.email.contains(keyword))
                            .or(seller.company_name.contains(keyword))
                            .or(seller.created_at.stringValue().contains(keyword)) // 가입일 숫자 검색
            );
        }

        return queryFactory
                .select(seller.count())
                .from(seller)
                .where(builder) // 필터링 조건 추가
                .fetchOne(); // 결과 반환
    }


}
