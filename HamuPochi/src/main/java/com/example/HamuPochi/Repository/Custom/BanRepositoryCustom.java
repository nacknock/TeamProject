package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;

public interface BanRepositoryCustom {
    long countByBuyer(Buyer buyer);

    long countBySeller(Seller seller);

    Ban findLastByBuyerId(long id);

    Ban findLastBySellerId(long id);

    void banOff(Ban ban);

    Ban findLastByBuyerEmail(String email);

    Ban findLastBySellerEmail(String email);
}
