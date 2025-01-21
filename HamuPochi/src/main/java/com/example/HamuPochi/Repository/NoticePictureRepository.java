package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Repository.Custom.NoticePictureRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.NoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticePictureRepository extends JpaRepository<NoticePicture,Long> , NoticePictureRepositoryCustom {
}
