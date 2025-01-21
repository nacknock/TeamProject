package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.DTO.QuestionDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface QuestionService {

    default Question DtoToEntity(QuestionDTO dto){
        Question entity =Question.builder()
                .id(dto.getId())
                .admin_id(dto.getAdmin_id())
                .buyer_id(dto.getBuyer_id())
                .seller_id(dto.getSeller_id())
                .title(dto.getTitle())
                .content(dto.getContent())
                .status(dto.isStatus())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .category(dto.getCategory())
                .build();

        return entity;
    }

    default QuestionDTO EntityToDTO(Question entity){
        QuestionDTO dto = QuestionDTO.builder()
                .id(entity.getId())
                .admin_id(entity.getAdmin_id())
                .buyer_id(entity.getBuyer_id())
                .seller_id(entity.getSeller_id())
                .title(entity.getTitle())
                .content(entity.getContent())
                .status(entity.isStatus())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .category(entity.getCategory())
                .build();

        return dto;
    }

    List<Question> SellerMainQuestion(Long seller_id);

    void save(QuestionDTO dto);


    List<Question> getListForAdmin(int count, long adminId);


    // 관리자 마이 페이지 : 유저 문의 list
    List<Question> getAdminQuestionList(Criteria cri);// 문의 목록
    long getAdminQuestionCount(Criteria cri);// 전체 문의 글 수

    Optional<Question> findOneById(Long id);

    void updateStatusTo1(Question questionId);

    List<Question> getSellerPageBuyerQna(Long id);
  
    List<Question> getBuyerPageQna(long id);

    List<Question> getSellerPageMyQna(Long id);
}
