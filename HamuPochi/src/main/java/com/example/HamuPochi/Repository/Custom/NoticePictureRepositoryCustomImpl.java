package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Entity.QAdmin;
import com.example.HamuPochi.Entity.QNoticePicture;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class NoticePictureRepositoryCustomImpl implements NoticePictureRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QNoticePicture noticePicture = QNoticePicture.noticePicture; // 기본 인스턴스 사용

    public NoticePictureRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void setFiles(Notice entity, List<String> filePaths) {
        for (String filePath : filePaths) {
            queryFactory
                    .insert(noticePicture)
                    .columns(
                            noticePicture.notice_id,
                            noticePicture.notice_picture_url
                    )
                    .values(
                            entity.getId(), // 상품 ID
                            filePath
                    )
                    .execute(); // 쿼리 실행
        }
    }

    @Override
    public void deleteByNoticeId(Long id) {
        queryFactory
                .delete(noticePicture)
                .where(noticePicture.notice_id.id.eq(id))
                .execute();
    }

    @Override
    public List<NoticePicture> getNoticePicture(Long id) {
        List<NoticePicture> list = queryFactory
                .select(noticePicture)
                .from(noticePicture)
                .where(noticePicture.notice_id.id.eq(id))
                .fetch();
        return list;
    }
}
