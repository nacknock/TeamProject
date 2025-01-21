package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Cart;
import com.example.HamuPochi.Repository.Custom.BanRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> , CartRepositoryCustom {
}
