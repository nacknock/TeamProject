package com.example.HamuPochi.Controller;

import com.example.HamuPochi.DTO.BanDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Service.BanService;
import com.example.HamuPochi.Service.BuyerService;
import com.example.HamuPochi.Service.SellerService;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user/*")
@RequiredArgsConstructor
@Log4j2
public class AdminUserController {

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final SellerService sellerService;

    @Autowired
    private final BanService banService;

    @GetMapping("list.do") // 유저 목록 조회 요청 처리
    public String userList(Model model, Criteria cri) {
        cri.setAmount(5); // 한 페이지에 보여줄 유저 수 설정

        // 구매자와 판매자 리스트 조회
        List<Object> userList = buyerService.getBuyerUserList(cri);

        // 총 사용자 수 조회
        long buyerCount = buyerService.getBuyerCount(cri); // 구매자 수 조회
        long sellerCount = sellerService.getSellerCount(cri); // 판매자 수 조회

        long totalCount = buyerCount + sellerCount; // 전체 유저 수 반환

        // 모델에 데이터 추가
        //thymeleaf ${buyer[0]} = email, ${buyer[1]} = name, ${buyer[2]} role, ${buyer[3]} created_at
        model.addAttribute("userList", userList); // 유저 리스트
        model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount)); // 페이지 정보
        model.addAttribute("cri", cri); // 검색 조건 정보
        model.addAttribute("totalCount", totalCount);

        return "Admin/user_list"; // 뷰 이름 반환
    }

    @GetMapping("detail.do")
    public String detail(Model model, @RequestParam("id") long id,@RequestParam("role") String role){

        if(role.equals("buyer")){
            Buyer buyer = buyerService.findById(id);
            Long count = banService.getCountByBuyer(buyer);
            model.addAttribute("count",count);

            if(Optional.ofNullable(buyer) != null){ //buyer인 경우

                model.addAttribute("role","buyer");
                model.addAttribute("buyer",buyer);

                long banCount = banService.getCountByBuyer(buyer);

                model.addAttribute("banCount",banCount);
            }
        }else{
            Optional<Seller> seller = sellerService.findById(id);
            Long count = banService.getCountBySeller(seller.get());
            model.addAttribute("count",count);

            if(seller.isPresent()){

                model.addAttribute("role","seller");
                model.addAttribute("seller",seller.get());

                long banCount = banService.getCountBySeller(seller.get());

                model.addAttribute("banCount",banCount);

            }
        }



        return "Admin/user_detail";
    }


    @PostMapping("ban.do")
    public String ban(Model model, BanDTO dto){

        dto.setStatus(true); // true = 밴됨, false = 밴 끝남
        banService.save(dto);

        return "redirect:/admin/user/list.do";
    }

//
//    @GetMapping("detail.do")
//    public String detail(Model model, @RequestParam("id") long id){
//
//
//
//        return "Admin/user_detail";
//    }

}
