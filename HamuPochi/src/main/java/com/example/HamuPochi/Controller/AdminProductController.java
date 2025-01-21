package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.ProductDTO;
import com.example.HamuPochi.DTO.ProductOptionDTO;
import com.example.HamuPochi.DTO.ProductPictureDTO;
import com.example.HamuPochi.Entity.Product;
import com.example.HamuPochi.Entity.ProductOption;
import com.example.HamuPochi.Entity.ProductPicture;
import com.example.HamuPochi.Service.*;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/product/*")
@RequiredArgsConstructor
@Log4j2
public class AdminProductController {



    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final SellerService sellerService;

    @Autowired
    private final ProductOrderService productOrderService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Autowired
    private final ProductPictureService productPictureService;

    @Autowired
    private final ProductService productService;

    @GetMapping("list.do") // 상품 목록 조회 요청 처리
    public String productList(Model model, Criteria cri) {
        cri.setAmount(5); // 한 페이지에 보여줄 상품 수 설정

        // 상품 리스트 조회
        List<ProductDTO> productList = productService.getProductList(cri);
        long totalCount = productService.getProductCount(cri);

        // 모델에 데이터 추가
        model.addAttribute("productList", productList); // 상품 리스트
        model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount)); // 페이지 정보
        model.addAttribute("cri", cri); // 검색 조건 정보

        return "Admin/product_list"; // 뷰 이름 반환
    }

    @GetMapping("detail.do")
    public String product_detail(@RequestParam("id") Long id, Model model) {

        Product product = Product.builder()
                .id(id)
                .build();

        Optional<Product> prodto = productService.readProduct(id);//product 불러오기
        Optional<List<ProductPicture>> pictures = productPictureService.findAllByProduct(product);
        Optional<List<ProductOption>> options = productOptionService.findAllByProduct(product);



        model.addAttribute("prodto", prodto.get());
        model.addAttribute("pictures", pictures.get());
        model.addAttribute("option", options.get());

        return "Admin/product_detail";

    }

    @GetMapping("delete.do")
    @Transactional
    public String productDelete(@RequestParam("id") Long id, Model model){

        productService.updateStatusById(id);

        return "redirect:/admin/product/list.do";
    }

}
