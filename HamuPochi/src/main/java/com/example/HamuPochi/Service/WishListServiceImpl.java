package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.DTO.WishListDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.WishList;
import com.example.HamuPochi.Repository.SellerRepository;
import com.example.HamuPochi.Repository.WishListRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;

    @Override
    public List<WishListDTO> getWishList(Long id) {
        List<WishListDTO> list = wishListRepository.getWishList(id);
        return list;
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            wishListRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

    @Override
    public void deleteById(List<Long> ids) {
        wishListRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public Optional<WishList> save(WishList entity) {
        WishList result = wishListRepository.save(entity);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<WishList> checkByBuyerAndProduct(Buyer buyer, long productId) {
        WishList entity = wishListRepository.findByBuyerAndProduct(buyer,productId);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<WishList> getWishListWithPaging(Criteria cri, long id) {
        List<WishList> list = wishListRepository.getWishListWithPaging(cri,id);
        return list;
    }

    @Override
    public long getTotalCount(Criteria cri, long id) {
        long count = wishListRepository.getCountForList(cri,id);
        return count;
    }


}
