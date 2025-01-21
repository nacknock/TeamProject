package com.example.HamuPochi.Service;

import com.example.HamuPochi.Config.SecurityConfig;
import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Repository.SellerRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SellerServiceImpl implements SellerService{

    private final SellerRepository SellerRepository;

    private final  SecurityConfig securityConfig;
    private final SellerRepository sellerRepository;

    @Override
    public String findByCompanyName(Long id) {
        String companyName = SellerRepository.findByCompanyName(id).get();
        return companyName;
    }

    @Override
    public boolean CheckByEmail(String email) {
        Optional<Seller> opEntity = SellerRepository.findByIdCustom(email);
        return opEntity.isPresent();//opEntity값이 null이면 false,not null이면 true return
    }

    @Override
    public boolean save(SellerDTO sellerDTO) {
        Seller entity = SellerRepository.save(DtoToEntity(sellerDTO));
        Optional<Seller> opEntity = java.util.Optional.ofNullable(entity);
        return opEntity.isPresent();//opEntity값이 null이면 false,not null이면 true return
    }

    @Override
    public boolean verifyPassword(Long id, String rawPassword) {
        Optional<Seller> seller = SellerRepository.findById(id);
        if(seller != null){
            return securityConfig.passwordEncoder().matches(rawPassword,seller.get().getPassword());
        }
        return false;
    }

    @Override
    public Optional<Seller> findById(Long id) {
        Optional<Seller> seller = SellerRepository.findById(id);
        return seller;
    }

    @Override
    public void sellerUpdate(SellerDTO dto) {
        SellerRepository.sellerUpdate(DtoToEntity(dto));
    }

    @Override
    public Seller checkByDTO(SellerDTO sellerDTO) {
        Seller entity = SellerRepository.findByDTO(sellerDTO);
        Optional<Seller> opEntity = java.util.Optional.ofNullable(entity);
        return opEntity.orElse(null);//opEntity값이 null이면 null,not null이면 entity return
    }

    @Override
    public Seller findByEmail(String email) {
        Optional<Seller> entity = SellerRepository.findByIdCustom(email);
        return entity.orElse(null);
    }
    @Override
    public void updatePw(Seller seller, String encodePwd) {
        SellerRepository.UpdatePwByEntity(seller,encodePwd);
    }
      
    public void withdrawal(Long id) {
        SellerRepository.withdrawal(id);
    }


    // 관리자 마이페이지: 유저정보목록 조회 기능을 위한 서비스
    @Override
    public List<SellerDTO> getSellerUserList(Criteria cri) {
        return sellerRepository.getSellerUserList(cri); // 판매자 유저 정보 목록 조회
    }

    @Override
    public long getSellerCount(Criteria cri) {
        return sellerRepository.countSellers(cri); // 판매자 수 조회
    }



    @Override
    public long totalCount() {
        long total = sellerRepository.count();
        return total;
    }


}
