package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Payment;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Repository.Custom.PaymentRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> , PaymentRepositoryCustom {
}
