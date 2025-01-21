package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.AdminDTO;
import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Admin;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    default Admin DtoToEntity(AdminDTO dto){
        Admin entity =Admin.builder()
                .id(dto.getId())
                .admin(dto.getAdmin())
                .password(dto.getPassword())
                .build();

        return entity;
    }

    default AdminDTO EntityToDTO(Admin entity){
        AdminDTO dto = AdminDTO.builder()
                .id(entity.getId())
                .admin(entity.getAdmin())
                .password(entity.getPassword())
                .build();

        return dto;
    }


}
