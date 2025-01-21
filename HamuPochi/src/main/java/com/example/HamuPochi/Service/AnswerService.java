package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.AnswerDTO;
import com.example.HamuPochi.Entity.Answer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AnswerService {

    default Answer DtoToEntity(AnswerDTO dto){
        Answer entity =Answer.builder()
                .id(dto.getId())
                .question_id(dto.getQuestion_id())
                .admin_id(dto.getAdmin_id())
                .seller_id(dto.getSeller_id())
                .content(dto.getContent())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default AnswerDTO EntityToDTO(Answer entity){
        AnswerDTO dto = AnswerDTO.builder()
                .id(entity.getId())
                .question_id(entity.getQuestion_id())
                .admin_id(entity.getAdmin_id())
                .seller_id(entity.getSeller_id())
                .content(entity.getContent())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    List<Answer> getSellerPageBuyerQna(Long id);

    List<Answer> getSellerPageMyQna(Long id);

    Optional<Answer> findOneByQuestionId(Long id);

    void save(AnswerDTO dto);

    List<Answer> getBuyerPageQna(Long id);
}
