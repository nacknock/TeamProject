package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Service.*;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j2
@Controller
@RequestMapping("/admin/notice/*")
@RequiredArgsConstructor
public class AdminNoticeController {


    @Autowired
    private final NoticeService noticeService;

    @Autowired
    private final NoticePictureService noticePictureService;

    @Autowired
    private final S3Service s3Service;

    @GetMapping("list.do") // 공지사항 목록 조회 요청 처리
    public String noticeList(Model model, Criteria cri) {
        cri.setAmount(5); // 한 페이지에 보여줄 공지사항 수 설정

        // 공지사항 리스트 조회
        List<Notice> noticeList = noticeService.getNoticeList(cri);
        long totalCount = noticeService.getNoticeCount(cri);

        // 모델에 데이터 추가
        model.addAttribute("noticeList", noticeList); // 공지사항 리스트
        model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount)); // 페이지 정보
        model.addAttribute("cri", cri); // 검색 조건 정보

        return "Admin/notice_list"; // 뷰 이름 반환
    }

    @GetMapping("detail.do")
    public String noticeDetail(Model model,@RequestParam("id") long id){
        Notice notice = noticeService.findOneById(id);//Notice 불러오기
        List<NoticePicture> picture = noticePictureService.getNoticePicture(id);//사진 불러오기

        model.addAttribute("notice", notice);
        model.addAttribute("picture", picture);

        return "Admin/notice_detail";
    }

    @GetMapping("write.do")
    public String noticeWrite(Model model, Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());

            return "Admin/notice_write";

        }
        return "Admin/notice_list";
    }

    @PostMapping("writeAction.do")
    public String writeAction(@ModelAttribute NoticeDTO dto,
                              @RequestParam("thumbnail") MultipartFile thumbnail,
                              @RequestParam("files") MultipartFile[] files) throws IOException {

        // S3에 업로드된 파일 경로 저장
        String thumbnailUrl = null;
        List<String> fileUrls = new ArrayList<>();
        NoticePictureDTO PictureDTO = null;

        // 썸네일 파일 업로드
        if (!thumbnail.isEmpty()) {
            String thumbnailKey = s3Service.uploadFile(thumbnail, "notice-thumbnails");
            thumbnailUrl = s3Service.getFileUrl(thumbnailKey);
            dto.setNotice_picture_url(thumbnailUrl);
        }

        // 메인 파일 업로드

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileKey = s3Service.uploadFile(file, "notice-images");
                fileUrls.add(s3Service.getFileUrl(fileKey));
            }
        }

        Notice entity = noticeService.save(dto);

        if (!fileUrls.isEmpty()) {
            noticePictureService.setFiles(entity, fileUrls);
        }


        return "redirect:/admin/notice/list.do";
    }

    @GetMapping("modify.do")
    public String noticeModify(Model model,@RequestParam("id") long id){
        Notice notice = noticeService.findOneById(id);//Notice 불러오기
        List<NoticePicture> picture = noticePictureService.getNoticePicture(id);//사진 불러오기

        model.addAttribute("notice", notice);
        model.addAttribute("picture", picture);

        return "Admin/notice_modify";
    }

    @PostMapping("modifyAction.do")
    public String modifyAction(@ModelAttribute NoticeDTO dto,
                               @RequestParam("thumbnail") MultipartFile thumbnail,
                               @RequestParam("files") MultipartFile[] files) throws IOException {

        // S3에 업로드된 파일 경로 저장
        String thumbnailUrl = null;
        List<String> fileUrls = new ArrayList<>();
        NoticePictureDTO PictureDTO = null;
        Notice notice = noticeService.findOneById(dto.getId());//Notice 불러오기

        // 썸네일 파일 업로드
        if (!thumbnail.isEmpty()) {
            String thumbnailKey = s3Service.uploadFile(thumbnail, "notice-thumbnails");
            thumbnailUrl = s3Service.getFileUrl(thumbnailKey);
            dto.setNotice_picture_url(thumbnailUrl);
        }else{
            dto.setNotice_picture_url(notice.getNotice_picture_url());
        }

        // 메인 파일 업로드

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileKey = s3Service.uploadFile(file, "notice-images");
                fileUrls.add(s3Service.getFileUrl(fileKey));
            }
        }

        Notice entity = noticeService.update(dto); // 공지 정보 저장

        if (!fileUrls.isEmpty()) {
            noticePictureService.deleteByNoticeId(entity.getId());//수정 전 원본 삭제
            noticePictureService.setFiles(entity, fileUrls);
        }

        return "redirect:/admin/notice/detail.do?id="+entity.getId();//detail로 리턴
    }

    @GetMapping("delete.do")
    public String delete(Model model,@RequestParam("id") long id){

        noticePictureService.deleteByNoticeId(id);
        noticeService.delete(id);

        return "redirect:/admin/notice/list.do";
    }
}
