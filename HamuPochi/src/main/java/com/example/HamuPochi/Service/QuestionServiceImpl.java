package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.QuestionDTO;
import com.example.HamuPochi.Entity.Answer;
import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Repository.QuestionRepository;
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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> SellerMainQuestion(Long seller_id) {
        return questionRepository.SellerMainQuestion(seller_id);
    }

    @Override
    public void save(QuestionDTO dto) {
        questionRepository.save(DtoToEntity(dto));
    }

    @Override
    public List<Question> getListForAdmin(int count, long adminId) {
        List<Question> list = questionRepository.getListForAdmin(count, adminId);
        return list;
    }

    // 관리자 마이 페이지 문의 리스트
    @Override
    public List<Question> getAdminQuestionList(Criteria cri) {
        return questionRepository.getAdminQuestionList(cri);
    }

    @Override
    public long getAdminQuestionCount(Criteria cri) {
        return questionRepository.getAdminQuestionCount(cri);
    }

    @Override
    public Optional<Question> findOneById(Long id) {
        Optional<Question> opEntity = questionRepository.findById(id);

        return opEntity;

    }

    @Override
    public void updateStatusTo1(Question question) {

        questionRepository.updateStatusTo1(question);

    }

    @Override
    public List<Question> getSellerPageBuyerQna(Long id) {
        List<Question> list = questionRepository.getSellerPageBuyerQna(id);
        return list;
    }

    public List<Question> getBuyerPageQna(long id) {
        List<Question> list = questionRepository.findAllByBuyerId(id);

        return list;
    }
  
    @Override
    public List<Question> getSellerPageMyQna(Long id) {
        List<Question> list = questionRepository.getSellerPageMyQna(id);
        return list;
    }

}
