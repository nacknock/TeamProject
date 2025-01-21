package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.Notice;
import com.example.HamuPochi.Entity.NoticePicture;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final NoticeService noticeService;

    @Autowired
    private final NoticePictureService noticePictureService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final MathService mathService;

    @GetMapping({"/","/main.do"})
    //이중으로 주고 싶으면 배열처리
    public String main(Model model, Authentication authentication,
                       @AuthenticationPrincipal SecurityDetails securityDetails) {
        
        //login user의 id 꺼내기 
        //Authentication authentication,@AuthenticationPrincipal SecurityDetails securityDetails
        //매개 변수 필요
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            log.info("@@@@@@@@@@@@@@@"+userDetails.getId());
            log.info("@@@@@@@@@@@@@@@"+userDetails.getAuthorities());
        }
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        //random select four notices
        List<Notice> noticeList4 = noticeService.findAllLimit(4);

        model.addAttribute("noticeList4",noticeList4);

        //select new product 7

        List<Product> newProductList = productService.findAllNewProductList(7);

        model.addAttribute("newProductList",newProductList);

        //random select three notices
        List<Notice> noticeList3 = noticeService.findAllLimit(3);
        model.addAttribute("noticeList3",noticeList3);

        //random category-product

        //random category setting
        long first = mathService.randomInt(categoryList.size());

        List<Product> randomProduct3 = productService.findAsCategoryLimit(4,3);

        model.addAttribute("randomProduct3",randomProduct3);

        long second = mathService.randomInt(categoryList.size(),first);

        List<Product> randomProduct6 = productService.findAsCategoryLimit(4,6);

        model.addAttribute("randomProduct6",randomProduct6);



        return "index";
    }
}
