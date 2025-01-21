package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.CartDTO;
import com.example.HamuPochi.Entity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartService {

    default Cart DtoToEntity(CartDTO dto){
        Cart entity =Cart.builder()
                .id(dto.getId())
                .option_id(dto.getOption_id())
                .buyer_id(dto.getBuyer_id())
                .amount(dto.getAmount())
                .created_at(dto.getCreated_at())
                .build();

        return entity;
    }

    default CartDTO EntityToDTO(Cart entity){
        CartDTO dto = CartDTO.builder()
                .id(entity.getId())
                .option_id(entity.getOption_id())
                .buyer_id(entity.getBuyer_id())
                .amount(entity.getAmount())
                .created_at(entity.getCreated_at())
                .build();

        return dto;
    }

    List<Cart> findAllByBuyerId(long id);

    boolean deleteCart(long id);

    Optional<Cart> insert(long buyerId, long id, long amount);

    Optional<Cart> checkByBuyer(long buyerId, long id);

    Cart findOneById(long cartId);

    void updateAmount(long id, long amount);
}
