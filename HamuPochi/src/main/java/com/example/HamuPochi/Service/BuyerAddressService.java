package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.BuyerAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BuyerAddressService {

    default BuyerAddress DtoToEntity(BuyerAddressDTO dto){
        BuyerAddress entity =BuyerAddress.builder()
                .id(dto.getId())
                .buyer_id(dto.getBuyer_id())
                .prefecture(dto.getPrefecture())
                .city(dto.getCity())
                .block_number(dto.getBlock_number())
                .building_name(dto.getBuilding_name())
                .post_number(dto.getPost_number())
                .status(dto.isStatus())
                .created_at(dto.getCreated_at())
                .build();

        return entity;
    }

    default BuyerAddressDTO EntityToDTO(BuyerAddress entity){
        BuyerAddressDTO dto = BuyerAddressDTO.builder()
                .id(entity.getId())
                .buyer_id(entity.getBuyer_id())
                .prefecture(entity.getPrefecture())
                .city(entity.getCity())
                .block_number(entity.getBlock_number())
                .building_name(entity.getBuilding_name())
                .post_number(entity.getPost_number())
                .status(entity.isStatus())
                .created_at(entity.getCreated_at())
                .build();

        return dto;
    }

    BuyerAddress findDefaultByBuyer(Buyer buyer);

    List<BuyerAddress> findAllByBuyer(Buyer buyer);

    List<BuyerAddressDTO> getAddressList(Long id);

    void addressDelete(Long id);

    Optional<BuyerAddress> save(BuyerAddress buyerAddress);

    BuyerAddress findOneById(long addressId);

    void updateById(BuyerAddress buyerAddress);
}
