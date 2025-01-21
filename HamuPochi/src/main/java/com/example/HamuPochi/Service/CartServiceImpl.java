package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.CartDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Cart;
import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Repository.CartRepository;
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
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    @Override
    public List<Cart> findAllByBuyerId(long id) {
        Optional<List<Cart>> list = cartRepository.findAllByBuyerId(id);
        return list.orElse(null);
    }

    @Override
    public boolean deleteCart(long id) {
        try {
            cartRepository.deleteById(id);
            return true; // 삭제 성공
        } catch (EmptyResultDataAccessException e) {
            return false; // 삭제 실패 (존재하지 않음)
        }
    }

    @Override
    public Optional<Cart> insert(long buyerId, long id, long amount) {

        Buyer buyer = Buyer.builder()
                .id(buyerId)
                .build();

        ProductOption option = ProductOption.builder()
                .id(id)
                .build();

        CartDTO dto = new CartDTO();
        dto.setAmount(amount);
        dto.setBuyer_id(buyer);
        dto.setOption_id(option);

        Cart entity = DtoToEntity(dto);

        entity = cartRepository.save(entity);

        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Cart> checkByBuyer(long buyerId, long id) {
        Cart entity = cartRepository.findOneByBuyerAndOption(buyerId,id);

        return Optional.ofNullable(entity);
    }

    @Override
    public Cart findOneById(long cartId) {

        Optional<Cart> entity = cartRepository.findById(cartId);

        return entity.orElse(null);
    }

    @Override
    public void updateAmount(long id, long amount) {
        cartRepository.updateAmount(id,amount);
    }
}
