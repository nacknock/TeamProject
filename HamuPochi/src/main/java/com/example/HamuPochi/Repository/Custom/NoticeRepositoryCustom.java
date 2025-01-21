package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Util.Criteria;

import java.util.List;

public interface NoticeRepositoryCustom {

    // 관리자 마이 페이지 : 관리자의 공지사항 list
    // 1.관리자의 공지사항 목록 조회
    List<Notice> getNoticeList(Criteria cri);
    // 2.관리자의 전체 공지사항 수 조회
    long getNoticeCount(Criteria cri);

    void update(Notice entity);

    List<Notice> findAllLimit(int limit);
}
