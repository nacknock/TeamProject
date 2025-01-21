package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.CartDTO;
import com.example.HamuPochi.Entity.Cart;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart/*")
@RequiredArgsConstructor
@Log4j2
public class CartController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryDetailService categoryDetailService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final CartService cartService;

    @GetMapping("list.do")
    public String cartList(Model model, Authentication authentication,
                           @AuthenticationPrincipal SecurityDetails securityDetails){
        //login user의 id 꺼내기
        //Authentication authentication,@AuthenticationPrincipal SecurityDetails securityDetails
        //매개 변수 필요
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            //get cartList
            List<Cart> cartList = cartService.findAllByBuyerId(userDetails.getId());
            model.addAttribute("cartList",cartList);

            // 추천 상품 리스트(4개) 가져오기
            if (!cartList.isEmpty()) {
                // 장바구니의 첫 번째 상품 객체 가져오기
                Cart firstCartItem = cartList.get(0);
                Product product = productService.findOne(firstCartItem.getOption_id().getProduct_id().getId()); // 첫 번째 상품 ID로 상품 정보 가져오기

                if (product != null) { // 상품이 null이 아닐 경우
                    Optional<List<Product>> recommendList = productService.getRecommList(product); // Product 객체를 사용하여 추천 상품 조회
                    recommendList.ifPresent(products -> model.addAttribute("recommend_list", products));
                }
            } else {
                List<Product> recommendList = productService.findAsCategoryLimit(4,4);
                model.addAttribute("recommend_list", recommendList);
            }

        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);




        return "product/cart";
    }

    @PostMapping("delete.do")
    @ResponseBody//ajax 통신으로 지우고 새로고침 없이 list에서 요소 삭제  //cartDTO 안에는 id만 있으면 됨
    public String deleteCart(Model model, @RequestParam("id[]") long[] ids, Authentication authentication,
                             @AuthenticationPrincipal SecurityDetails securityDetails){
        if (authentication != null) {
            //cartDelete
            boolean bool = false;
            for(long id : ids){
                if(cartService.deleteCart(id)){
                    bool = true;
                }
            }

            if(bool){
                return "true";
            }else{
                return "false";
            }
        }

        return "false";
    }

    @PostMapping("add.do")
    @ResponseBody// id = option_id
    public String cartInsert(@RequestParam("id") long id,@RequestParam("amount") long amount,
                             Authentication authentication,
                             @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            long buyerId = userDetails.getId();
            
            Optional<Cart> cart = cartService.checkByBuyer(buyerId,id);//카트에 이미 등록되어있는지 체크
            
            if(!cart.isPresent()){ //결과 없음 == 등록되어있지 않음
                cart = cartService.insert(buyerId,id,amount);

                if(cart.isPresent()){
                    return "true";
                }else{
                    return "false";
                }
            }else{
                return "added";
            }
            
        }

        return "false";
    }

    @PostMapping("changeAmount.do")
    @ResponseBody
    public String changeAmount(@RequestParam("id") long id,@RequestParam("amount") long amount,
                               Authentication authentication,
                               @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            long buyerId = userDetails.getId();

            try{
                cartService.updateAmount(id,amount);
                return "true";
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }

        }

        return "error";
    }

}
