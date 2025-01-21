package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;
import java.util.Optional;

public interface SellerRepositoryCustom {
        Optional<Seller> findByIdCustom(String username);

        //회사명 출력
        Optional<String> findByCompanyName(Long id);

        void sellerUpdate(Seller entity);

        void withdrawal(Long id);
  
        Seller findByDTO(SellerDTO sellerDTO);

        Seller findByIdPw(String email, String encodePwd);

        void UpdatePwByEntity(Seller seller, String encodePwd);


        //관리자 마이페이지 : 유저정보관리 리스트 조회 기능을 위한 리포지터리들
        // 1.판매자 목록 조회
        List<SellerDTO> getSellerUserList(Criteria cri);
        // 2. 전체 판매자 유저 수
        long countSellers(Criteria cri);
}
