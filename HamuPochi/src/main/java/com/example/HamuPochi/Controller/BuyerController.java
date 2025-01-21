package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Service.*;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/buyer/*")
@RequiredArgsConstructor
@Log4j2
public class BuyerController {



    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductOrderService productOrderService;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final AnswerService answerService;

    @Autowired
    private final BuyerAddressService buyerAddressService;

    @Autowired
    private final WishListService wishListService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final QuestionService questionService;

    //구매자 메인 주문 리스트
    @GetMapping("main.do")
    public String orderList(Criteria cri, Model model, Authentication authentication,
                            @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            long id = userDetails.getId();
            //@RequestParam("id") 구매자 id
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);

            cri.setAmount(5);
            List<ProductOrderDTO> list = productOrderService.getBuyerPageOrderList(cri, id);
            long totalCount = productOrderService.getBuyerPageOrderCount(cri, id);
            model.addAttribute("list", list);
            model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount));
            model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달

            System.out.println("Order List: " + list.size());
        }


        return "buyer/buyerMain";
    }

    //date 검색 페이징 비동기식
    @PostMapping("list_paging")//= buyer Id
    public String list_paging(Criteria cri, Model model, Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails) {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();

            log.info("cri:"+cri.getPageNum()+"//"+cri.getOffset());

            cri.setAmount(5);
            List<ProductOrderDTO> list = productOrderService.getBuyerPageOrderList(cri, id);
            long totalCount = productOrderService.getBuyerPageOrderCount(cri, id);
            model.addAttribute("list", list);
            model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount));
            model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달
        }

        return "Fragments/buyerOrderList :: list";//공지사항 올린 shop 프로젝트 중 detail.html과 fragments/review 참고
    }

    //리뷰 등록
    @PostMapping("review.do")
    @ResponseBody
    public String review(@ModelAttribute ReviewDTO dto, Authentication authentication,
                         @AuthenticationPrincipal SecurityDetails securityDetails) {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            Buyer entity = buyerService.findById(id);
            Optional<ProductOrder> productOrder = productOrderService.findByBuyerAndProduct(id, dto.getProduct_id());

            if (!productOrder.isPresent()) {//구매하지 않았을 경우
                return "notBuy";
            }
            dto.setBuyer_id(entity);
            int result = reviewService.setReview(dto);

            if (result > 0) {
                return "true";
            } else {
                return "false";
            }
        }
        return "false";
    }

    //리뷰 삭제
    @PostMapping("reviewDel.do")
    @ResponseBody
    public String reviewDel(@RequestParam("review_id") long review_id, Authentication authentication,
                         @AuthenticationPrincipal SecurityDetails securityDetails) {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();

            try {
                reviewService.deleteOneById(review_id);
                return "true";
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }


        }
        return "false";
    }

    //정보변경 전 비밀번호 입력 페이지
    @GetMapping("password.do")
    public String enterPasswordPage(Model model, Authentication authentication,
                                    @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);
        }
        return "buyer/buyerPwChk";
    }

    //비밀번호 확인 기능
    @PostMapping("passwordCheck")
    @ResponseBody
    public String passwordChk(@RequestParam("password") String password, HttpSession session,
                              Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            if (buyerService.verifyPassword(id, password)) {
                session.setAttribute("pw_checked", 33);//세션 등록
                return "true";
            }else{
                return "false";
            }
        }
        return "error";
    }

    //구매자 정보 페이지
    @GetMapping("information.do")
    public String information(Model model, HttpSession session, Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            //user id값
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);
//            if(session.getAttribute("pw_checked") != null){
//                int pwChk = (int) session.getAttribute("pw_checked");//password 체크했는지 확인
//
//                if (pwChk == 33) {
                    Buyer buyer = buyerService.findById(id);
                    model.addAttribute("Buyer", buyer);
                    return "buyer/buyerInformation";
//                }
//            }

        }
        return "buyer/buyerInformation";
    }

    //구매자 정보 수정
    @PostMapping("informationModify.do")
    @ResponseBody
    public String informationModify(BuyerDTO dto, @RequestParam("passwordChk") String passwordChk,
                                    Authentication authentication,
                                    @AuthenticationPrincipal SecurityDetails securityDetails) {
        log.info("dto : "+dto);
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            dto.setId(id);
            try {
                if(passwordChk != "" && dto.getPassword() != ""){
                    if (passwordChk.equals(dto.getPassword()) && !dto.getPassword().equals("")) {
                        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
                        buyerService.buyerUpdate(dto);
                        return "true";
                    } else {
                        return "notEqual";
                    }
                }
                Buyer buyer = buyerService.findById(dto.getId());
                dto.setPassword(buyer.getPassword());
                buyerService.buyerUpdate(dto);
                return "true";
            }catch(Exception e){
                e.printStackTrace();
                return "error";
            }

        }
        return "error";

    }

    //판매자 회원 탈퇴
    @GetMapping("withdrawal.do")
    public String withdrawal(@RequestParam("id") Long id) {
        // @RequestParam("id") 판매자 id

        buyerService.withdrawal(id);

        return "redirect:/logout.do";
    }

    //qna 리스트
    @GetMapping("qna.do")
    public String QnaList(Model model, Authentication authentication,
                          @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);

            List<Question> list = questionService.getBuyerPageQna(id);
            List<Answer> aList = answerService.getBuyerPageQna(id);
            List<QuestionDTO> qList = new ArrayList<QuestionDTO>();
            for(Question entity : list){
                QuestionDTO dto = questionService.EntityToDTO(entity);
                for(Answer answer : aList){
                    if(answer.getQuestion_id().getId() == dto.getId()){
                        dto.setAnswer(answer);
                    }
                }
                qList.add(dto);
                log.info("dto :"+dto);
            }

            model.addAttribute("qList", qList);

        }
        return "buyer/buyerQnaList";
    }

    //주소 리스트
    @GetMapping("address.do")
    public String addressList(Model model, Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);

            List<BuyerAddressDTO> list = buyerAddressService.getAddressList(id);
            model.addAttribute("list", list);
        }
        return "buyer/buyerAddressList";

    }

    //결제 페이지에서 배송지 변경
    @PostMapping("addressChange.do")
    public String addressChange(Model model, @RequestParam("id") long address_id,
                                Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            model.addAttribute("id", id);

            BuyerAddress entity = buyerAddressService.findOneById(address_id);
            model.addAttribute("address", entity);

            log.info("addddd:"+entity);
        }
        return "Fragments/address";

    }

    //주소 삭제
    @PostMapping("addressDelete.do")
    @ResponseBody
    public String addressDelete(@RequestParam("id") long id) {
        try{
            log.info("@@@id:"+id);
            BuyerAddress entity = buyerAddressService.findOneById(id);

            if(entity.isStatus()){

                buyerAddressService.addressDelete(id);
                return "true";
            }else{
                log.info("@@2@id:"+id);
                return "cannot";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    //주소 등록
    @PostMapping("addAddress.do")
    @ResponseBody
    public String addAddress(BuyerAddressDTO dto) {
        try {
            BuyerAddress defaultAddress = buyerAddressService.findDefaultByBuyer(dto.getBuyer_id());
            if (defaultAddress != null && !defaultAddress.isStatus()) { // 기본 주소가 있는 경우
                dto.setStatus(true); // 1 대신 true 사용
            } else {
                dto.setStatus(false);
            }

            BuyerAddress buyerAddress = buyerAddressService.DtoToEntity(dto);


            Optional<BuyerAddress> entity = buyerAddressService.save(buyerAddress);

            return "" + entity.get().getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    //주소 수정
    @PostMapping("updateAddress.do")
    @ResponseBody
    public String updateAddress(BuyerAddressDTO dto) {
        try {

            BuyerAddress buyerAddress = buyerAddressService.DtoToEntity(dto);

            buyerAddressService.updateById(buyerAddress);

            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    //찜목록
    @GetMapping("wishList.do")
    public String wishList(Model model,Criteria cri, Authentication authentication,
                           @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            model.addAttribute("id", id);

            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);

            cri.setAmount(5);
            List<WishList> list = wishListService.getWishListWithPaging(cri,id);
            long totalCount = wishListService.getTotalCount(cri,id);
            model.addAttribute("list", list);
            model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
            model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달
        }
        return "buyer/buyerWishList";
    }

    //선택 모든 찜 삭제
    @PostMapping("wishDelete.do")
    @ResponseBody//ajax 통신으로 지우고 새로고침 없이 list에서 요소 삭제  //cartDTO 안에는 id만 있으면 됨
    public String wishAllDelete(@RequestParam("ids[]") List<Long> ids) {
        //@RequestParam("id") wish id
        boolean bool = false;
        for (Long id : ids) {
            // ID별로 삭제 수행
            log.info("ids :" + id);
            if(wishListService.deleteById(id)){
                bool = true;
            }
        }

        if(bool){
            return "true";
        }else{
            return "false";
        }
    }

    //찜 등록
    @PostMapping("addWish.do")
    @ResponseBody //@RequestParam("id") long id = productId
    public String addWish(@RequestParam("id") long id, Authentication authentication,
                          @AuthenticationPrincipal SecurityDetails securityDetails) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null && (principal instanceof SecurityDetails)) {
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(id);
            Product product = productService.DtoToEntity(productDTO);

            Buyer buyer = buyerService.findById(userDetails.getId());

            if (Optional.ofNullable(buyer) == null) {//로그인은 했지만 구매자가 아닌 경우
                return "notBuyer";
            }

            Optional<WishList> checkOp = wishListService.checkByBuyerAndProduct(buyer, id); // 이미 추가되었는지 여부

            if (!checkOp.isPresent()) {
                WishListDTO dto = new WishListDTO();
                dto.setProduct_id(product);
                dto.setBuyer_id(buyer);
                WishList entity = wishListService.DtoToEntity(dto);

                Optional<WishList> wishList = wishListService.save(entity);

                if (wishList.isPresent()) {//성공
                    return "true";
                } else {
                    return "false";
                }
            } else {
                return "added";
            }


        }

        return "notLogin";

    }

    @PostMapping("statusUpdate.do")
    @ResponseBody
    public String statusUpdate(ProductOrderDTO dto, Authentication authentication,
                               @AuthenticationPrincipal SecurityDetails securityDetails){

        log.info("dtttto:"+dto);

        if (authentication != null) {
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            try{
                if(!dto.isStatus()){
                    productOrderService.updateStatusToTrue(dto.getId());
                    return "true";
                }else{
                    return "false";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }


        }
        return "false";
    }





}
