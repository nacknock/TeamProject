package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SellerService {


    //categoryId 추가 2024/12/29
    default Seller DtoToEntity(SellerDTO dto){
        Seller entity =Seller.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .company_name(dto.getCompany_name())
                .company_address(dto.getCompany_address())
                .bank_name(dto.getBank_name())
                .bank_account(dto.getBank_account())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .category_id(dto.getCategory_id())
                .build();

        return entity;
    }
    //categoryId 추가 2024/12/29
    default SellerDTO EntityToDTO(Seller entity){
        SellerDTO dto = SellerDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .name(entity.getName())
                .company_name(entity.getCompany_name())
                .company_address(entity.getCompany_address())
                .bank_name(entity.getBank_name())
                .bank_account(entity.getBank_account())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .category_id(entity.getCategory_id())
                .build();

        return dto;
    }

    String findByCompanyName(Long id);


    boolean CheckByEmail(String email);

    boolean save(SellerDTO sellerDTO);

    boolean verifyPassword(Long id, String rawPassword);

    Optional<Seller> findById(Long id);

    void sellerUpdate(SellerDTO dto);

    Seller checkByDTO(SellerDTO sellerDTO);

    Seller findByEmail(String email);

    void updatePw(Seller seller, String encodePwd);
  
    void withdrawal(Long id);


    // 관리자 마이페이지 : 유저 정보 목록 조회 기능을 위한 서비스
    // 판매자 유저 정보 목록 조회
    List<SellerDTO> getSellerUserList(Criteria cri);
    // 전체 판매자 유저 수
    long getSellerCount(Criteria cri);

    long totalCount();
}
