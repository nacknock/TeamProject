package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Repository.NoticeRepository;
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
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

    @Override
    public Notice readNotice(Long id) {
        Optional<Notice> entity = noticeRepository.findById(id);

        return entity.orElse(null);
    }


    // 관리자의 마이페이지 : 관리자의 공지사항 list
    @Override
    public List<Notice> getNoticeList(Criteria cri) {
        return noticeRepository.getNoticeList(cri); // 관리자의 공지사항 목록
    }

    @Override
    public long getNoticeCount(Criteria cri) {
        return noticeRepository.getNoticeCount(cri); // 관리자의 공지사항 글 수
    }

    @Override
    public Notice findOneById(long id) {
        Optional<Notice> entity = noticeRepository.findById(id);

        return entity.get();
    }

    @Override
    public Notice save(NoticeDTO dto) {
        Notice entity = DtoToEntity(dto);
        entity = noticeRepository.save(entity);
        return entity;
    }

    @Override
    public Notice update(NoticeDTO dto) {
        Notice entity = DtoToEntity(dto);
        noticeRepository.update(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public List<Notice> findAllLimit(int limit) {
        List<Notice> list = noticeRepository.findAllLimit(limit);
        return list;
    }

}
