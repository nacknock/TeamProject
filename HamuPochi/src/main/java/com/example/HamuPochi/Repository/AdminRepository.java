package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Repository.Custom.AdminRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.BuyerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> , AdminRepositoryCustom {
}
