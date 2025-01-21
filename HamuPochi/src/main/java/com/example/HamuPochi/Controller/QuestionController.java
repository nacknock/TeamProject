package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.AdminDTO;
import com.example.HamuPochi.DTO.QuestionDTO;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/question/*")
@Log4j2
@RequiredArgsConstructor
@Controller
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final SellerService sellerService;

    @GetMapping("write.do")//질문 작성 페이지
    public String writeQuestion(Model model, @RequestParam(name="id",defaultValue = "0") long productId, Authentication authentication,
                                @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());

            if(productId != 0){ //to Seller from Buyer
                Product product = productService.findOne(productId);
                model.addAttribute("product",product);
                model.addAttribute("role",2);//role을 통해 id input의 name 결정 buyer_id

            }else{ //to admin from Seller or Buyer
                String email = userDetails.getUsername();
                if(buyerService.CheckByEmail(email)){//구매자
                    model.addAttribute("role",0);//role을 통해 id input의 name 결정 buyer_id
                }else if(sellerService.CheckByEmail(email)){//판매자
                    model.addAttribute("role",1);//role을 통해 id input의 name 결정 seller_id

                }
            }
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        return "product/qnaWrite";
    }

    @PostMapping("save.do")
    public String questionSave(@ModelAttribute QuestionDTO dto,
                               @RequestParam("role") long role, Model model){

        if(role == 2){ //to Seller from Buyer

            questionService.save(dto);

            return "redirect:/buyer/qna.do";
        }else{ //to admin from Seller or Buyer
            //admin id setting
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(1l);
            //questionDTO admin_id setting
            dto.setAdmin_id(adminService.DtoToEntity(adminDTO));

            if(role == 0){ // from buyer to admin

                questionService.save(dto);

                return "redirect:/buyer/qna.do";
            }else if(role == 1){ // from seller to admin

                questionService.save(dto);

                return "redirect:/seller/myQna.do";
            }else if(role == 2){ // from buyer to seller

                questionService.save(dto);

                return "redirect:/buyer/qna.do";
            }
        }
        return "index";
    }
}

