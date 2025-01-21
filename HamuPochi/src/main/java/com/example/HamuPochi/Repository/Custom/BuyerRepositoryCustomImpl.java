package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.QBuyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public class BuyerRepositoryCustomImpl implements BuyerRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QBuyer buyer = QBuyer.buyer; // 기본 인스턴스 사용

    public BuyerRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Buyer> findByIdCustom(String username) {

        Buyer result =queryFactory
                .select(buyer)
                .from(buyer)
                .where(buyer.email.eq(username))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Buyer findByDTO(BuyerDTO buyerDTO) {

        Buyer result =queryFactory
                .select(buyer)
                .from(buyer)
                .where(buyer.name.eq(buyerDTO.getName())
                        .and(buyer.phone_number.eq(buyerDTO.getPhone_number()))
                )
                .fetchOne();

        return result;

    }

    @Override
    public void UpdatePwByEntity(Buyer entity, String encodePwd) {

            queryFactory
                    .update(buyer)
                    .set(buyer.password,encodePwd)
                    .where(buyer.id.eq(entity.getId()))
                    .execute();
    }

    //관리자 마이페이지 유저정보 목록 조회를 위한 메서드
    // 1. 구매자 유저 정보 목록 조회
    // 구매자 목록 조회 메서드
    @Override
    public List<Object> getBuyerUserList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();
        String sql = "";
        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            sql = "SELECT email, name, '購買者' AS role, created_at, id " +
                    "FROM buyer " +
                    "where name like '%" + cri.getKeyword() + "%'" +
                    "UNION ALL " +
                    "SELECT email, name, '販売者' AS role, created_at,id " +
                    "FROM seller " +
                    "where name like '%" + cri.getKeyword() + "%'" +
                    "ORDER BY created_at DESC " +
                    "LIMIT "+cri.getAmount()+" OFFSET "+ cri.getOffset();
        }else {
            sql = "SELECT email, name, 'buyer' AS role, created_at,id " +
                    "FROM buyer " +
                    "UNION ALL " +
                    "SELECT email, name, 'seller' AS role, created_at,id " +
                    "FROM seller " +
                    "ORDER BY created_at DESC " +
                    "LIMIT "+cri.getAmount()+" OFFSET "+ cri.getOffset();
        }

        Query query = em.createNativeQuery(sql);
        List<Object> results = query.getResultList();

        return results;
    }

    //전체 구매자 유저 수 조회
    @Override
    public long countBuyers(Criteria cri) {
        QBuyer buyer = QBuyer.buyer; // QBuyer 인스턴스 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    buyer.name.contains(keyword)
                            .or(buyer.email.contains(keyword))
                            .or(buyer.nickname.contains(keyword))
                            .or(buyer.created_at.stringValue().contains(keyword)) // 가입일 숫자 검색
            );
        }

        return queryFactory
                .select(buyer.count())
                .from(buyer)
                .where(builder) // 필터링 조건 추가
                .fetchOne(); // 결과 반환
    }

    @Override
    public void buyerUpdate(Buyer entity){
        queryFactory
                .update(buyer)
                .set(buyer.nickname,entity.getNickname())
                .set(buyer.password,entity.getPassword())
                .set(buyer.name,entity.getName())
                .set(buyer.phone_number,entity.getPhone_number())
                .where(buyer.id.eq(entity.getId()))
                .execute();
    }


}
