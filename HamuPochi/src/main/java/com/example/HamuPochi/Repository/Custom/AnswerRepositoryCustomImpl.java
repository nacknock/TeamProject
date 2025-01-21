package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.AnswerDTO;
import com.example.HamuPochi.Entity.Answer;
import com.example.HamuPochi.Entity.QAnswer;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class AnswerRepositoryCustomImpl implements AnswerRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QAnswer answer = QAnswer.answer; // 기본 인스턴스 사용

    public AnswerRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Answer> getSellerPageBuyerQna(Long id) {
        List<Answer> list =queryFactory
                .select(answer)
                .from(answer)
                .where(answer.seller_id.id.eq(id).and(answer.admin_id.isNull()))
                .orderBy(answer.id.desc())
                .fetch();
        return list;
    }

    @Override
    public List<Answer> getSellerPageMyQna(Long id) {
        List<Answer> list =queryFactory
                .select(answer)
                .from(answer)
                .where(answer.seller_id.id.eq(id))
                .orderBy(answer.id.desc())
                .fetch();
        return list;
    }

    @Override
    public Optional<Answer> findByQuestionId(Long id) {
        Answer entity = queryFactory
                .select(answer)
                .from(answer)
                .where(answer.question_id.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<Answer> getBuyerPageQna(Long id) {
        List<Answer> list =queryFactory
                .select(answer)
                .from(answer)
                .where(answer.question_id.buyer_id.id.eq(id))
                .orderBy(answer.question_id.created_at.desc())
                .fetch();
        return list;
    }

}
