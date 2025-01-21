package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.QQuestion;
import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QQuestion question = QQuestion.question; // 기본 인스턴스 사용

    public QuestionRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Question> SellerMainQuestion(Long seller_id) {
        List<Question> questionList =queryFactory
                .select(question)
                .from(question)
                .where(question.seller_id.id.eq(seller_id))
                .orderBy(question.id.desc())
                .limit(3)
                .fetch();
        return questionList;
    }

    @Override
    public List<Question> getSellerPageBuyerQna(Long id) {
        List<Question> list =queryFactory
                .select(question)
                .from(question)
                .where(question.seller_id.id.eq(id).and(question.admin_id.isNull()))
                .orderBy(question.id.desc())
                .fetch();
        return list;
    }

    @Override
    public List<Question> getSellerPageMyQna(Long id) {
        List<Question> list =queryFactory
                .select(question)
                .from(question)
                .where(question.seller_id.id.eq(id).and(question.buyer_id.isNull()))
                .orderBy(question.id.desc())
                .fetch();
        return list;
    }

    @Override
    public List<Question> getListForAdmin(int count, long adminId) {
        List<Question> questionList =queryFactory
                .select(question)
                .from(question)
                .where(question.admin_id.id.eq(adminId))
                .orderBy(question.created_at.desc())
                .limit(count)
                .fetch();
        return questionList;
    }

    // 문의 목록 조회 메서드
    public List<Question> getAdminQuestionList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    question.title.contains(keyword) // 문의 제목 검색
            );
        }

        return queryFactory
                .select(question)
                .from(question)
                .where(builder.and(question.admin_id.id.eq(1l))) // 필터링 조건 추가
                .orderBy(question.created_at.desc()) // 생성일 기준 내림차순 정렬
                .offset(cri.getOffset()) // 페이징을 위해 오프셋 설정
                .limit(cri.getAmount()) // 페이지당 출력할 항목 수 설정
                .fetch(); // 결과 반환
    }

    // 전체 문의 수 조회 메서드
    public long getAdminQuestionCount(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    question.title.contains(keyword)
                            .or(question.content.contains(keyword))
                            .or(question.buyer_id.name.contains(keyword))
                            .or(question.seller_id.name.contains(keyword))
            );
        }

        return queryFactory
                .select(question.count())
                .from(question)
                .where(builder.and(question.admin_id.id.eq(1l))) // 필터링 조건 추가
                .fetchOne(); // 결과 반환
    }

    @Override
    public void updateStatusTo1(Question entity) {

        queryFactory.update(question)
                .set(question.status,true)
                .where(question.id.eq(entity.getId()))
                .execute();
    }

    @Override
    public List<Question> findAllByBuyerId(long id) {

        List<Question> list = queryFactory
                .select(question)
                .from(question)
                .where(question.buyer_id.id.eq(id))// buyer_id 조건
                .orderBy(question.id.desc())
                .fetch();

        return list;
    }


}
