package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public interface BuyerService {

    default Buyer DtoToEntity(BuyerDTO dto){
        Buyer entity =Buyer.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .name(dto.getName())
                .phone_number(dto.getPhone_number())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default BuyerDTO EntityToDTO(Buyer entity){
        BuyerDTO dto = BuyerDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .phone_number(entity.getPhone_number())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    boolean CheckByEmail(String email);

    Buyer findById(long id);

    boolean save(BuyerDTO buyerDTO);

    Buyer checkByDTO(BuyerDTO buyerDTO);

    Optional<Buyer> findByEmail(String email);

    void updatePw(Buyer buyer, String encodePwd);


    boolean existsByEmail(String email);

    long totalCount();

    // 관리자 마이페이지 : 유저 정보 목록 조회 기능 구현을 위한 서비스
    // 구매자 정보 목록 서비스
    List<Object> getBuyerUserList(Criteria cri);
    // 전체 구매자 수
    long getBuyerCount(Criteria cri);

    boolean verifyPassword(Long id, String rawPassword);

    void buyerUpdate(BuyerDTO dto);

    Optional<Buyer> findById(Long id);

    void withdrawal(Long id);

}
