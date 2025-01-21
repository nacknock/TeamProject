package com.example.HamuPochi.Service;

import com.example.HamuPochi.Config.SecurityConfig;
import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Repository.BuyerRepository;
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
public class BuyerServiceImpl implements BuyerService{

    private final BuyerRepository buyerRepository;

    private final SecurityConfig securityConfig;

    @Override
    public boolean CheckByEmail(String email) {
        Optional<Buyer> opEntity = buyerRepository.findByIdCustom(email);
        return opEntity.isPresent();//opEntity값이 null이면 false,not null이면 true return
    }

    @Override
    public Buyer findById(long id) {
        Optional<Buyer> entity = buyerRepository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public boolean save(BuyerDTO buyerDTO) {
        Buyer entity = buyerRepository.save(DtoToEntity(buyerDTO));
        Optional<Buyer> opEntity = java.util.Optional.ofNullable(entity);
        return opEntity.isPresent();//opEntity값이 null이면 false,not null이면 true return
    }

    @Override
    public Buyer checkByDTO(BuyerDTO buyerDTO) {
        Buyer entity = buyerRepository.findByDTO(buyerDTO);
        Optional<Buyer> opEntity = java.util.Optional.ofNullable(entity);
        return opEntity.orElse(null);//opEntity값이 null이면 null,not null이면 entity return

    }

    @Override
    public Optional<Buyer> findByEmail(String email) {
        return buyerRepository.findByIdCustom(email);
    }


    @Override
    public void updatePw(Buyer buyer, String encodePwd) {
        buyerRepository.UpdatePwByEntity(buyer,encodePwd);
    }

    @Override
    public boolean existsByEmail(String email) {
        return buyerRepository.existsByEmail(email);
    }


    public long totalCount() {
        long total = buyerRepository.count();
        return total;
    }


    // 관리자 마이페이지 : 유저 정보 목록 조회 기능 구현을 위한 서비스
    @Override
    public List<Object> getBuyerUserList(Criteria cri) {
        return buyerRepository.getBuyerUserList(cri); // 구매자 정보 리스트
    }
    @Override
    public long getBuyerCount(Criteria cri) {
        return buyerRepository.countBuyers(cri); // 구매자 수 조회
    }

    @Override
    public boolean verifyPassword(Long id, String rawPassword) {
        Optional<Buyer> buyer = buyerRepository.findById(id);
        if(buyer != null){
            return securityConfig.passwordEncoder().matches(rawPassword,buyer.get().getPassword());
        }
        return false;
    }

    @Override
    public void buyerUpdate(BuyerDTO dto) {
        buyerRepository.buyerUpdate(DtoToEntity(dto));
    }

    @Override
    public Optional<Buyer> findById(Long id) {
        Optional<Buyer> buyer = buyerRepository.findById(id);
        return buyer;
    }

    @Override
    public void withdrawal(Long id) {
        buyerRepository.deleteById(id);
    }

}
