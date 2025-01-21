package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Util.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {

    default Notice DtoToEntity(NoticeDTO dto){
        Notice entity =Notice.builder()
                .id(dto.getId())
                .admin_id(dto.getAdmin_id())
                .title(dto.getTitle())
                .content(dto.getContent())
                .notice_picture_url(dto.getNotice_picture_url())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .build();

        return entity;
    }

    default NoticeDTO EntityToDTO(Notice entity){
        NoticeDTO dto = NoticeDTO.builder()
                .id(entity.getId())
                .admin_id(entity.getAdmin_id())
                .title(entity.getTitle())
                .content(entity.getContent())
                .notice_picture_url(entity.getNotice_picture_url())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();

        return dto;
    }

    Notice readNotice(Long id);

    // 관리자의 마이페이지 : 관리자의 공지사항 list
    //1. 관리자의 공지사항 목록
    List<Notice> getNoticeList(Criteria cri);
    //2. 관리자의 공지사항 글 수
    long getNoticeCount(Criteria cri);

    Notice findOneById(long id);

    Notice save(NoticeDTO dto);

    Notice update(NoticeDTO dto);

    void delete(long id);

    List<Notice> findAllLimit(int limit);
}
