package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepositoryCustom {
    Optional<List<Cart>> findAllByBuyerId(long id);

    Cart findOneByBuyerAndOption(long buyerId, long id);

    void updateAmount(long id, long amount);
}
