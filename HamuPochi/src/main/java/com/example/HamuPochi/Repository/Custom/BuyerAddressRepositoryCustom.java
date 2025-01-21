package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.BuyerAddress;

import java.util.List;
import java.util.Optional;

public interface BuyerAddressRepositoryCustom {
    Optional<BuyerAddress> findDefaultByBuyer(Buyer buyer);

    Optional<List<BuyerAddress>> findAllByBuyer(Buyer buyer);

    List<BuyerAddressDTO> getAddressList(Long id);

    void updateById(BuyerAddress buyerAddress);
}
