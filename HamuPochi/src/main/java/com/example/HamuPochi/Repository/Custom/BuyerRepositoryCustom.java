package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;
import java.util.Optional;

public interface BuyerRepositoryCustom {
    Optional<Buyer> findByIdCustom(String username);

    Buyer findByDTO(BuyerDTO buyerDTO);

    void UpdatePwByEntity(Buyer buyer, String encodePwd);

    //관리자 마이페이지 유저정보 목록 조회를 위한 메서드
    // 1. 구매자 유저 정보 목록 조회
    List<Object> getBuyerUserList(Criteria cri);
    //2. 전체 구매자 유저 수
    long countBuyers(Criteria cri);

    void buyerUpdate(Buyer entity);

}
