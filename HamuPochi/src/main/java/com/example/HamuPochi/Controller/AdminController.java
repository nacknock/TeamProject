package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Service.*;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/admin/*")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final ProductOrderService productOrderService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final SellerService sellerService;

    @Autowired
    private final QuestionService questionService;
  
    @Autowired
    private NoticeService noticeService;


    @GetMapping({"/","/main.do"})
    public String main(Model model,Authentication authentication,
                       @AuthenticationPrincipal SecurityDetails securityDetails){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());

            //전체 상품 수
            long productCnt = productService.totalCount();
            model.addAttribute("productCnt",productCnt);
            //전체 주문 수
            long orderCnt = productOrderService.totalCount();
            model.addAttribute("orderCnt",orderCnt);
            //전체 회원 수
            long buyerCnt = buyerService.totalCount();
            long sellerCnt = sellerService.totalCount();
            long userCnt = buyerCnt+sellerCnt;
            model.addAttribute("userCnt",userCnt);
            //최신 질문(to Admin) 8개
            List<Question> questionList = questionService.getListForAdmin(8,userDetails.getId());
            model.addAttribute("questionList",questionList);
            //주문 비율
            HashMap<String,Long> orderRate = productOrderService.getAllOrderRate();//카테고리,비율이 담긴 Map
            model.addAttribute("orderRate",orderRate);
            for (Map.Entry<String, Long> entry : orderRate.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue();
                log.info("@@@@@@+"+key+"////"+value);
            }
            //오늘 기준 제일 많이 팔린 상품 5개
            LinkedHashMap<Product, Long> topSellingList = productOrderService.getTopSelling(5);
            //product_id(Product entity)만 담겼음 topSellingList.product_id.? 로 부를것
            model.addAttribute("topSellingList",topSellingList);

            for (Map.Entry<Product, Long> entry : topSellingList.entrySet()) {
                Product product = entry.getKey();
                Long value = entry.getValue();
                System.out.println("Product: " + product.getTitle() + ", value: " + value);
            }

            log.info("dto :"+productCnt+","+orderCnt+","+orderRate.size()+","+buyerCnt+","+questionList.size()+","+topSellingList.size());

        }


        return "Admin/index";
    }
}
