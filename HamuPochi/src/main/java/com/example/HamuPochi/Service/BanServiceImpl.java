package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BanDTO;
import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Repository.BanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BanServiceImpl implements BanService{

    private final BanRepository banRepository;

    @Override
    public void save(BanDTO dto) {
        banRepository.save(DtoToEntity(dto));
    }

    @Override
    public long getCountByBuyer(Buyer buyer) {
        long banCount = banRepository.countByBuyer(buyer);
        return banCount;
    }

    @Override
    public long getCountBySeller(Seller seller) {
        long banCount = banRepository.countBySeller(seller);
        return banCount;
    }

    @Override
    public Ban findLastByBuyerId(long id) {
        Ban entity = banRepository.findLastByBuyerId(id);
        return entity;
    }

    @Override
    public Ban findLastBySellerId(long id) {
        Ban entity = banRepository.findLastBySellerId(id);
        return entity;
    }

    @Override
    public void banOff(Ban ban) {
        banRepository.banOff(ban);
    }

    @Override
    public Optional<Ban> findLastByBuyerEmail(String email) {

        Ban ban = banRepository.findLastByBuyerEmail(email);

        return Optional.ofNullable(ban);
    }

    @Override
    public Optional<Ban> findLastBySellerEmail(String email) {
        Ban ban = banRepository.findLastBySellerEmail(email);

        return Optional.ofNullable(ban);
    }
}
