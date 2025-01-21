package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BanDTO;
import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BanService {

    default Ban DtoToEntity(BanDTO dto){
        Ban entity =Ban.builder()
                .id(dto.getId())
                .seller_id(dto.getSeller_id())
                .buyer_id(dto.getBuyer_id())
                .reason(dto.getReason())
                .status(dto.isStatus())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default BanDTO EntityToDTO(Ban entity){
        BanDTO dto = BanDTO.builder()
                .id(entity.getId())
                .seller_id(entity.getSeller_id())
                .buyer_id(entity.getBuyer_id())
                .reason(entity.getReason())
                .status(entity.isStatus())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    void save(BanDTO dto);

    long getCountByBuyer(Buyer buyer);

    long getCountBySeller(Seller seller);

    Ban findLastByBuyerId(long id);

    Ban findLastBySellerId(long id);

    void banOff(Ban ban);

    Optional<Ban> findLastByBuyerEmail(String email);

    Optional<Ban> findLastBySellerEmail(String email);
}
