package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.*;
import com.example.HamuPochi.Service.*;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product/*")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final CategoryDetailService categoryDetailService;

    @Autowired
    private final ProductPictureService productPictureService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Autowired
    private final ProductNoticeService productNoticeService;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final BuyerAddressService buyerAddressService;

    @Autowired
    private final CartService cartService;

    @Autowired
    private final WishListService wishListService;

    @Autowired
    private final SellerService sellerService;

    //메인 페이지 상품 리스트 get
    @GetMapping("list.do")
    public String productList(Criteria cri, Model model, Authentication authentication,
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
        }
        log.info(12312123);
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        cri.setAmount(9);
        List<ProductDTO> list = productService.getListWithPaging(cri);
        long totalCount = productService.getTotalCount(cri);
        model.addAttribute("list", list);
        model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
        model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달
        return "product/productList";
    }

    //상품 상세 페이지
    @GetMapping("detail.do")//@RequestParam("id") = product Id
    public String productDetail(@RequestParam("id") long id,Model model,Authentication authentication,
                                @AuthenticationPrincipal SecurityDetails securityDetails){

        long userId = 0l;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            userId = userDetails.getId();

            model.addAttribute("id",userId);
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        Product product = productService.findOne(id);
        if(product != null){ //product(해당 상품)이 null이 아닌 경우
            //상세 물건
            ProductDTO productDTO = productService.EntityToDTO(product);
            model.addAttribute("productDTO",productDTO);
            
            //썸네일 외 사진
            Optional<List<ProductPicture>> pictureList = productPictureService.findAllByProduct(product);
            if(pictureList.isPresent()){
                model.addAttribute("pictureList",pictureList.get());
            }
            
            //옵션 status=1인것만
            Optional<List<ProductOption>> optionList = productOptionService.findAllByProduct(product);
            if(optionList.isPresent()){
                model.addAttribute("optionList",optionList.get());
            }

            //공지
            Optional<List<ProductNotice>> noticeList = productNoticeService.findAllByProduct(product);
            if(noticeList.isPresent()){
                model.addAttribute("noticeList",noticeList.get());
            }

            //리뷰
            Criteria cri = new Criteria();
            cri.setAmount(5);
            Optional<List<Review>> reviewList = reviewService.getListWithPagingByProduct(cri,product);
            long totalCount = reviewService.getTotalCount(cri,product);
            model.addAttribute("reviewTotal",totalCount);
            model.addAttribute("reviewList", reviewList.get());
            model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
            model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달
            double raitAvg = reviewService.getraitAvg(product);//리뷰 평균 점수
            model.addAttribute("raitAvg",raitAvg);

            //추천상품 리스트(4개)
            Optional<List<Product>> recommend_list = productService.getRecommList(product);
            if(recommend_list.isPresent()){
                model.addAttribute("recommend_list",recommend_list.get());
            }

            //판매자 구분
            boolean bool = false;

            if(userId == product.getSeller_id().getId()){
                if(sellerService.findById(userId).isPresent()){
                    bool = true;
                }
            }
            log.info("@@@@123 : "+userId+"//"+product.getSeller_id().getId()+"//"+bool);
            model.addAttribute("bool",bool);

            return "product/productDetail";
        }
        return "product/productList";
    }
    
    @PostMapping("review_paging")//ajax review 비동기 페이징 @RequestParam("id") = product Id
    public String review_paging(Criteria cri, Model model,
                                @RequestParam("id") long id){

        log.info("@@@@@@@"+cri.getOffset()+"///"+cri.getPageNum()+"///"+cri.getAmount());

        Product product = productService.findOne(id);
        cri.setAmount(5);
        Optional<List<Review>> reviewList = reviewService.getListWithPagingByProduct(cri,product);
        long totalCount = reviewService.getTotalCount(cri,product);
        model.addAttribute("reviewList", reviewList.get());
        model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
        model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달

        ProductDTO dto = productService.EntityToDTO(product);

        model.addAttribute("productDTO",dto);

        return "fragments/productDetailReview :: review";//공지사항 올린 shop 프로젝트 중 detail.html과 fragments/review 참고
    }

    @GetMapping("checkout.do")//구매수량은 productDTO의 amount
    public String checkout(@RequestParam("id") long optionId, @RequestParam("amount") long buyerAmount, Model model,
                           ArrayList<CartDTO> cartDTOList, Authentication authentication,
                           @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) { //로그인 정보가 있는 경우
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            long id = userDetails.getId();//로그인 유저 id(long)

            model.addAttribute("id",id);//로그인 유저 id(long)
            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList",categoryList);

            //buyer setting
            Buyer buyer = buyerService.findById(id);

            if(buyer == null){
                return "redirect:/product/list.do";
            }

            model.addAttribute("buyer",buyer);

            //buyer 기반으로 주소 검색
            List<BuyerAddress> buyerAddressList = buyerAddressService.findAllByBuyer(buyer);

            if(buyerAddressList != null){//주소가 있는 경우
                //기본 주소 자리에는 th:if로 status가 0인 것 넣을 것, 0 아닌 것들은 modal에
                model.addAttribute("addressList",buyerAddressList);
            }
            CartDTO cartDTO = new CartDTO();
            //단일 구매
            if(cartDTOList == null || cartDTOList.size() == 0){
                log.info("cartcccc"+cartDTOList.size());
                //cart setting
                ProductOption option = productOptionService.findById(optionId);
                log.info("option:"+option);

                cartDTO.setBuyer_id(buyer);
                cartDTO.setOption_id(option);
                cartDTO.setAmount(buyerAmount);
                log.info("cartcart"+cartDTO);

            }

            cartDTOList.add(cartDTO);
            model.addAttribute("cartDTOList",cartDTOList);

            long total = 0l;
            for(CartDTO dto : cartDTOList){
                total += dto.getOption_id().getProduct_id().getPrice()*dto.getAmount();
            }
            log.info("totla: "+total);
            model.addAttribute("total",total);

            return "product/checkout";


        }


        return "product/productList";
    }

    @PostMapping("checkout.do")//구매수량은 productDTO의 amount
    public String checkoutPost(Model model,@RequestParam("id[]") long[] cartIds, Authentication authentication,
                           @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) { //로그인 정보가 있는 경우
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            long id = userDetails.getId();//로그인 유저 id(long)

            model.addAttribute("id",id);//로그인 유저 id(long)
            //select * from category / header 카테고리
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList",categoryList);

            //buyer setting
            Buyer buyer = buyerService.findById(id);

            model.addAttribute("buyer",buyer);

            if(buyer == null){
                return "product/productList";
            }
            //buyer 기반으로 주소 검색
            List<BuyerAddress> buyerAddressList = buyerAddressService.findAllByBuyer(buyer);

            if(buyerAddressList != null){//주소가 있는 경우
                //기본 주소 자리에는 th:if로 status가 0인 것 넣을 것, 0 아닌 것들은 modal에
                model.addAttribute("addressList",buyerAddressList);
            }else{
                model.addAttribute("addressList",null);
            }
            List<Cart> cartList = new ArrayList<Cart>();

            for(long cartId : cartIds){
                log.info("long :"+cartId);
                Cart cart = cartService.findOneById(cartId);
                log.info("cartt :"+cart);
                cartList.add(cart);
            }
            model.addAttribute("cartDTOList",cartList);

            long total = 0l;
            for(Cart entity : cartList){
                total += entity.getOption_id().getProduct_id().getPrice()*entity.getAmount();
            }
            model.addAttribute("total",total);

            return "product/checkout";


        }


        return "product/productList";
    }

    @PostMapping("tempSaveOrder")
    @ResponseBody
    public String tempSaveOrder(BuyerAddressDTO dto, @RequestParam("option_id") long[] option_id,
                                @RequestParam("amount") long[] amount,Model model, HttpSession session){
        try {
            log.info("temtem:" + dto);

            String address = "〒"+dto.getPost_number()+dto.getPrefecture()+dto.getCity();
            if(dto.getBlock_number() != null){
                address += dto.getBlock_number();
            }
            if(dto.getBuilding_name() != null){
                address += dto.getBuilding_name();
            }
            dto.setPrefecture(address);

            session.setAttribute("receiver", dto);
            session.setAttribute("option_id", option_id);
            session.setAttribute("cartAmount", amount);
            return "true";
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }
    }

    @PostMapping("delete.do")
    @ResponseBody
    public String delete(Model model,@RequestParam("id") long id){

        try{

            productService.deleteOneById(id);

            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }


}
