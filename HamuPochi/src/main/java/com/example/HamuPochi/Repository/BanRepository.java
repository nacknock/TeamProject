package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Repository.Custom.BanRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.BuyerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends JpaRepository<Ban,Long> , BanRepositoryCustom {
}
