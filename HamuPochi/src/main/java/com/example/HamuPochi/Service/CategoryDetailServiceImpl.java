package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.CategoryDetailDTO;
import com.example.HamuPochi.Entity.CategoryDetail;
import com.example.HamuPochi.Repository.CategoryDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CategoryDetailServiceImpl implements CategoryDetailService {

    private final CategoryDetailRepository categoryDetailRepository;

    @Override
    public List<CategoryDetailDTO> getAllCategoryDetails() {
        // 데이터베이스에서 모든 CategoryDetail 엔티티 가져오기
        List<CategoryDetail> entities = categoryDetailRepository.findAll();

        // 엔티티를 DTO로 변환
        List<CategoryDetailDTO> categoryDetails = entities.stream()
                .map(this::EntityToDTO) // 인터페이스의 EntityToDTO 메서드 사용
                .collect(Collectors.toList());

        // 변환된 DTO 리스트 로그 출력
        log.info("Fetched Category Details: {}", categoryDetails);

        return categoryDetails;
    }
}