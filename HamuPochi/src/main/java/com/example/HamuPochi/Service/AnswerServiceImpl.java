package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.AnswerDTO;
import com.example.HamuPochi.Entity.Answer;
import com.example.HamuPochi.Repository.AnswerRepository;
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
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;

    private final AdminService adminService;

    public void asv(){
    }

    @Override
    public List<Answer> getSellerPageBuyerQna(Long id) {
        List<Answer> list = answerRepository.getSellerPageBuyerQna(id);
        return list;
    }

    @Override
    public List<Answer> getSellerPageMyQna(Long id) {
        List<Answer> list = answerRepository.getSellerPageMyQna(id);
        return list;
    }

    @Override
    public Optional<Answer> findOneByQuestionId(Long id) {
        Optional<Answer> opEntity = answerRepository.findByQuestionId(id);

        return opEntity;
    }

    @Override
    public void save(AnswerDTO dto) {
        answerRepository.save(DtoToEntity(dto));
    }

    @Override
    public List<Answer> getBuyerPageQna(Long id) {
        List<Answer> list = answerRepository.getBuyerPageQna(id);
        return list;
    }
}
