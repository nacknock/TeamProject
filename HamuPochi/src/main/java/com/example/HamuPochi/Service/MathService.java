package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.AdminDTO;
import com.example.HamuPochi.Entity.Admin;
import org.springframework.stereotype.Service;

@Service
public interface MathService {


    long randomInt(int size);

    long randomInt(int size, long first);
}
