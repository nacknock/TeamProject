package com.example.HamuPochi.Repository.Custom;

import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Entity.Product;

import java.util.List;

public interface NoticePictureRepositoryCustom {
    void setFiles(Notice entity, List<String> filePaths);

    void deleteByNoticeId(Long id);

    List<NoticePicture> getNoticePicture(Long id);
}
