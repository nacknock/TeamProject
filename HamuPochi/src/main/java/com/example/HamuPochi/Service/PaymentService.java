package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.AdminDTO;
import com.example.HamuPochi.DTO.PaymentDTO;
import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    default Payment DtoToEntity(PaymentDTO dto){
        Payment entity =Payment.builder()
                .id(dto.getId())
                .payment_number(dto.getPayment_number())
                .approved_at(dto.getApproved_at())
                .total_payment(dto.getTotal_payment())
                .build();

        return entity;
    }

    default PaymentDTO EntityToDTO(Payment entity){
        PaymentDTO dto =PaymentDTO.builder()
                .id(entity.getId())
                .payment_number(entity.getPayment_number())
                .approved_at(entity.getApproved_at())
                .total_payment(entity.getTotal_payment())
                .build();

        return dto;
    }


    Payment save(PaymentDTO paymentDTO);
}
