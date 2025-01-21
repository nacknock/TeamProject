package com.example.HamuPochi.Controller;


import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.NoticeDTO;
import com.example.HamuPochi.DTO.NoticePictureDTO;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Service.CategoryService;
import com.example.HamuPochi.Service.NoticePictureService;
import com.example.HamuPochi.Service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/notice/*")
@RequiredArgsConstructor
public class NoticeController {


    @Autowired
    private final NoticeService noticeService;
    @Autowired
    private final NoticePictureService noticePictureService;
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("detail.do")
    public String notice_detail(@RequestParam("id") long id, Model model,
                              Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails){

        Notice entity = noticeService.readNotice(id);
        NoticeDTO dto = noticeService.EntityToDTO(entity);

        List<NoticePicture> ntcimg = noticePictureService.getNoticePicture(id);

        model.addAttribute("dto", dto);
        model.addAttribute("ntcpicture", ntcimg);

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        return "noticeDetail";

    }


}
