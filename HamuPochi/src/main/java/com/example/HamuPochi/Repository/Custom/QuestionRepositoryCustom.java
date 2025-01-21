package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;

public interface QuestionRepositoryCustom {

    List<Question> SellerMainQuestion(Long seller_id);

    List<Question> getSellerPageBuyerQna(Long id);

    List<Question> getSellerPageMyQna(Long id);

    List<Question> getListForAdmin(int count, long adminId);

    // 관리자 마이페이지 : 유저 문의 list
    // 문의 목록 조회
    List<Question> getAdminQuestionList(Criteria cri);

    // 전체 문의 수 조회
    long getAdminQuestionCount(Criteria cri);


    void updateStatusTo1(Question question);

    List<Question> findAllByBuyerId(long id);
}
