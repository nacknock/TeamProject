package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.DTO.NoticePictureDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticePictureService {

    default NoticePicture DtoToEntity(NoticePictureDTO dto){
        NoticePicture entity =NoticePicture.builder()
                .id(dto.getId())
                .notice_id(dto.getNotice_id())
                .notice_picture_url(dto.getNotice_picture_url())
                .created_at(dto.getCreated_at())
                .build();

        return entity;
    }

    default NoticePictureDTO EntityToDTO(NoticePicture entity){
        NoticePictureDTO dto = NoticePictureDTO.builder()
                .id(entity.getId())
                .notice_id(entity.getNotice_id())
                .notice_picture_url(entity.getNotice_picture_url())
                .created_at(entity.getCreated_at())
                .build();

        return dto;
    }

    List<NoticePicture> getNoticePicture(Long id);

    void deleteByNoticeId(Long id);

    void setFiles(Notice entity, List<String> filePaths);

}
