package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.AnswerDTO;
import com.example.HamuPochi.Entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepositoryCustom {


    List<Answer> getSellerPageBuyerQna(Long id);

    List<Answer> getSellerPageMyQna(Long id);

    Optional<Answer> findByQuestionId(Long id);

    List<Answer> getBuyerPageQna(Long id);
}
