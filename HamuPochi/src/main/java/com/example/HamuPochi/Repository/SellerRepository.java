package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Repository.Custom.BanRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.SellerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> , SellerRepositoryCustom {
}
