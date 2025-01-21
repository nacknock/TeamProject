package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.DTO.WishListDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Entity.WishList;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface WishListService {

    default WishList DtoToEntity(WishListDTO dto){
        WishList entity =WishList.builder()
                .id(dto.getId())
                .product_id(dto.getProduct_id())
                .buyer_id(dto.getBuyer_id())
                .created_at(dto.getCreated_at())
                .build();

        return entity;
    }

    default WishListDTO EntityToDTO(WishList entity){
        WishListDTO dto = WishListDTO.builder()
                .id(entity.getId())
                .product_id(entity.getProduct_id())
                .buyer_id(entity.getBuyer_id())
                .created_at(entity.getCreated_at())
                .build();

        return dto;
    }

    List<WishListDTO> getWishList(Long id);

    boolean deleteById(Long id);

    void deleteById(List<Long> ids);

    Optional<WishList> save(WishList entity);

    Optional<WishList> checkByBuyerAndProduct(Buyer buyer, long id);

    List<WishList> getWishListWithPaging(Criteria cri, long id);

    long getTotalCount(Criteria cri, long id);
}
