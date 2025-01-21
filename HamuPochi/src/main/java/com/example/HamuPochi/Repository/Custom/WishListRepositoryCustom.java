package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.WishListDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.WishList;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;

public interface WishListRepositoryCustom {

    List<WishListDTO> getWishList(Long id);

    WishList findByBuyerAndProduct(Buyer buyer, long productId);

    List<WishList> getWishListWithPaging(Criteria cri, long id);

    long getCountForList(Criteria cri, long id);
}
