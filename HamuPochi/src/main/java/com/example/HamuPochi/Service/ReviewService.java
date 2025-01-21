package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.QuestionDTO;
import com.example.HamuPochi.DTO.ReviewDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Entity.Review;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReviewService {

    default Review DtoToEntity(ReviewDTO dto){
        Review entity =Review.builder()
                .id(dto.getId())
                .buyer_id(dto.getBuyer_id())
                .product_id(dto.getProduct_id())
                .content(dto.getContent())
                .rating(dto.getRating())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default ReviewDTO EntityToDTO(Review entity){
        ReviewDTO dto = ReviewDTO.builder()
                .id(entity.getId())
                .buyer_id(entity.getBuyer_id())
                .product_id(entity.getProduct_id())
                .content(entity.getContent())
                .rating(entity.getRating())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    Optional<List<Review>> findAllByProduct(Product product);

    Optional<List<Review>> getListWithPagingByProduct(Criteria cri, Product product);

    long getTotalCount(Criteria cri, Product product);

    int setReview(ReviewDTO dto);

    double getraitAvg(Product product);

    void deleteOneById(long reviewId);
}
