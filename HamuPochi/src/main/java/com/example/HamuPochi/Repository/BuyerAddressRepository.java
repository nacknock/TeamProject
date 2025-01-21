package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Ban;
import com.example.HamuPochi.Entity.BuyerAddress;
import com.example.HamuPochi.Repository.Custom.BanRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.BuyerAddressRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerAddressRepository extends JpaRepository<BuyerAddress,Long> , BuyerAddressRepositoryCustom {
}
