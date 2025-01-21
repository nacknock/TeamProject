package com.example.HamuPochi.Service;

import com.example.HamuPochi.DTO.NoticePictureDTO;
import com.example.HamuPochi.DTO.ProductPictureDTO;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Repository.NoticePictureRepository;
import com.example.HamuPochi.Repository.NoticeRepository;
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
public class NoticePictureServiceImpl implements NoticePictureService{

    private final NoticePictureRepository noticePictureRepository;


    @Override
    public List<NoticePicture> getNoticePicture(Long id) {
        List<NoticePicture> list =  noticePictureRepository.getNoticePicture(id);
        return list;
    }

    @Override
    public void deleteByNoticeId(Long id) {
        noticePictureRepository.deleteByNoticeId(id);
    }

    @Override
    public void setFiles(Notice entity, List<String> filePaths) {
        NoticePictureDTO dto = new NoticePictureDTO();
        dto.setNotice_id(entity);
        for(String filePath : filePaths){
            dto.setNotice_picture_url(filePath);
            noticePictureRepository.save(DtoToEntity(dto));
        }
    }

}
