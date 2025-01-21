package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.BuyerAddressDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.BuyerAddress;
import com.example.HamuPochi.Repository.BuyerAddressRepository;
import com.example.HamuPochi.Repository.BuyerRepository;
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
public class BuyerAddressServiceImpl implements BuyerAddressService{

    private final BuyerAddressRepository buyerAddressRepository;

    @Override
    public BuyerAddress findDefaultByBuyer(Buyer buyer) {

        Optional<BuyerAddress> entity = buyerAddressRepository.findDefaultByBuyer(buyer);

        return entity.orElse(null);
    }

    @Override
    public List<BuyerAddress> findAllByBuyer(Buyer buyer) {
        Optional<List<BuyerAddress>> list = buyerAddressRepository.findAllByBuyer(buyer);
        return list.orElse(null);
    }

    @Override
    public List<BuyerAddressDTO> getAddressList(Long id) {

        List<BuyerAddressDTO> list = buyerAddressRepository.getAddressList(id);

        return list;
    }

    @Override
    public void addressDelete(Long id) {
        buyerAddressRepository.deleteById(id);
    }

    @Override
    public Optional<BuyerAddress> save(BuyerAddress entity) {

        BuyerAddress result = buyerAddressRepository.save(entity);

        return Optional.ofNullable(result);
    }

    @Override
    public BuyerAddress findOneById(long addressId) {
        Optional<BuyerAddress> entity = buyerAddressRepository.findById(addressId);

        return entity.orElse(null);
    }

    @Override
    public void updateById(BuyerAddress buyerAddress) {
        buyerAddressRepository.updateById(buyerAddress);
    }
}
