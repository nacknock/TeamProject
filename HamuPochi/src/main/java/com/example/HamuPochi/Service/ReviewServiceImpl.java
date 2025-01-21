package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ReviewDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.Review;
import com.example.HamuPochi.Repository.QuestionRepository;
import com.example.HamuPochi.Repository.ReviewRepository;
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
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public Optional<List<Review>> findAllByProduct(Product product) {
        Optional<List<Review>> list = reviewRepository.findAllByProduct(product);
        return list;
    }

    @Override
    public Optional<List<Review>> getListWithPagingByProduct(Criteria cri, Product product) {
        Optional<List<Review>> list = reviewRepository.findAllPagingByProduct(cri,product);
        return list;
    }

    @Override
    public long getTotalCount(Criteria cri, Product product) {
        long result = reviewRepository.getTotalCount(cri,product);
        return result;
    }

    @Override
    public int setReview(ReviewDTO dto) {
        Review entity = DtoToEntity(dto);
        log.info("DDD:"+entity.getProduct_id()+"///"+dto.getProduct_id());
        Review savedEntity = reviewRepository.save(entity);
        return (int) savedEntity.getBuyer_id().getId();
    }

    @Override
    public double getraitAvg(Product product) {
        //해당 물건에 달린 리뷰 중 별점만 가져오기
        List<Double> RaitList = reviewRepository.getRaitByProduct(product);

        if(RaitList.size() == 0){
            return 0l;
        }

        double raitTot = 0l;
        double raitAvg = 0.0;

        for(Double raitOne : RaitList){
            raitTot = (raitTot + raitOne);
        }

        raitAvg = Math.round(raitTot/RaitList.size()*10)/10.0;

        return raitAvg;
    }

    @Override
    public void deleteOneById(long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
