package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.BuyerAddress;
import com.example.HamuPochi.Entity.QBuyerAddress;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class BuyerAddressRepositoryCustomImpl implements BuyerAddressRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QBuyerAddress buyerAddress = QBuyerAddress.buyerAddress; // 기본 인스턴스 사용

    public BuyerAddressRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override //유저의 기본 주소 받아오기
    public Optional<BuyerAddress> findDefaultByBuyer(Buyer buyer) {

        BuyerAddress entity = queryFactory
                .select(buyerAddress)
                .from(buyerAddress)
                .where(buyerAddress.buyer_id.eq(buyer)
                        .and(buyerAddress.status.eq(false)))
                .fetchOne();

        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<List<BuyerAddress>> findAllByBuyer(Buyer buyer) {

        List<BuyerAddress> list = queryFactory
                .select(buyerAddress)
                .from(buyerAddress)
                .where(buyerAddress.buyer_id.eq(buyer))
                .orderBy(buyerAddress.status.asc(),buyerAddress.created_at.asc())
                .fetch();

        return Optional.ofNullable(list);
    }

    @Override
    public List<BuyerAddressDTO> getAddressList(Long id) {
        List<BuyerAddressDTO> list = queryFactory
                .select(Projections.fields(BuyerAddressDTO.class,buyerAddress))
                .from(buyerAddress)
                .where(buyerAddress.buyer_id.id.eq(id))
                .fetch();
        return list;
    }

    @Override
    public void updateById(BuyerAddress entity) {
        queryFactory
                .update(buyerAddress)
                .set(buyerAddress.post_number, entity.getPost_number())
                .set(buyerAddress.prefecture,entity.getPrefecture())
                .set(buyerAddress.city, entity.getCity())
                .set(buyerAddress.block_number,entity.getBlock_number())
                .set(buyerAddress.building_name,entity.getBuilding_name())
                .where(buyerAddress.id.eq(entity.getId()))
                .execute();
    }
}
