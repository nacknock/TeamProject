package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.QNotice;
import com.example.HamuPochi.Util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QNotice notice = QNotice.notice; // 기본 인스턴스 사용

    public NoticeRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    // 관리자 마이페이지 : 관리자의 공지사항 list
    // 관리자의 공지사항 목록 조회 메서드
    public List<Notice> getNoticeList(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    notice.title.contains(keyword) // 공지사항 제목 검색
            );
        }

        return queryFactory
                .select(notice)
                .from(notice)
                .where(builder) // 필터링 조건 추가
                .orderBy(notice.created_at.desc()) // 생성일 기준 내림차순 정렬
                .offset(cri.getOffset()) // 페이징을 위해 오프셋 설정
                .limit(cri.getAmount()) // 페이지당 출력할 항목 수 설정
                .fetch(); // 결과 반환
    }

    // 전체 공지사항 수 조회 메서드
    public long getNoticeCount(Criteria cri) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있으면 필터링
        if (cri.getKeyword() != null && !cri.getKeyword().isEmpty()) {
            String keyword = cri.getKeyword();
            builder.and(
                    notice.title.contains(keyword)
                            .or(notice.content.contains(keyword))
            );
        }

        return queryFactory
                .select(notice.count())
                .from(notice)
                .where(builder) // 필터링 조건 추가
                .fetchOne(); // 결과 반환
    }

    @Override
    public void update(Notice entity) {
        queryFactory
                .update(notice)
                .set(notice.title,entity.getTitle())
                .set(notice.notice_picture_url,entity.getNotice_picture_url())
                .set(notice.content,entity.getContent())
                .where(notice.id.eq(entity.getId()))
                .execute();
    }

    // for index, random 4rows select
    @Override
    public List<Notice> findAllLimit(int limit) {
        List<Notice> list = queryFactory
                .select(notice)
                .from(notice)
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc()) // 랜덤 정렬(MySQL)
                .limit(limit)
                .fetch();

        return list;
    }


}
