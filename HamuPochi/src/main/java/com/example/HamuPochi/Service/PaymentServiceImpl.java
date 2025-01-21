package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.PaymentDTO;
import com.example.HamuPochi.DTO.ProductNoticeDTO;
import com.example.HamuPochi.Entity.Payment;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductNotice;
import com.example.HamuPochi.Repository.Custom.PaymentRepositoryCustom;
import com.example.HamuPochi.Repository.PaymentRepository;
import com.example.HamuPochi.Repository.ProductNoticeRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(PaymentDTO paymentDTO) {
        Payment entity = DtoToEntity(paymentDTO);
        entity = paymentRepository.save(entity);
        return entity;
    }
}
