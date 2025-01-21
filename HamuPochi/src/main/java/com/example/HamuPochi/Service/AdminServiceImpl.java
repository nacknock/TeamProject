package com.example.HamuPochi.Service;

import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Repository.AdminRepository;
import com.example.HamuPochi.Repository.BuyerRepository;
import com.example.HamuPochi.Repository.SellerRepository;
import com.example.HamuPochi.Util.Criteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;
}
