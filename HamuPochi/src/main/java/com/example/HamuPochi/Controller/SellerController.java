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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/seller/*")
@RequiredArgsConstructor
@Log4j2
public class SellerController {



    @Autowired
    private final S3Service s3Service;

    @Autowired
    private final SellerService sellerService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final CategoryDetailService categoryDetailService;

    @Autowired
    private final ProductPictureService productPictureService;

    @Autowired
    private final ProductNoticeService productNoticeService;

    @Autowired
    private final ProductOptionService productOptionService;

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final ProductOrderService productOrderService;

    @Autowired
    private final AnswerService answerService;

    @Autowired
    private final RekognitionService rekognitionService;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    //메인페이지
    @GetMapping("main.do")
    public String sellerMyPage(Model model,Authentication authentication,
                               @AuthenticationPrincipal SecurityDetails securityDetails) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }
        //메인 페이지 사업자명 출력
        String companyName = sellerService.findByCompanyName(id);
        model.addAttribute("companyName", companyName);
        //공지사항리스트
        List<ProductNotice> noticeList = productNoticeService.getMainNoticeList(id);
        model.addAttribute("noticeList", noticeList);
        //문의리스트
        List<Question> questionList = questionService.SellerMainQuestion(id);
        model.addAttribute("questionList", questionList);

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        //판매량 그래프
        HashMap<String,Long> orderRate = productOrderService.getAllOrderRateForSeller(id);//상품명,비율이 담긴 Map
        model.addAttribute("orderRate",orderRate);
        for (Map.Entry<String, Long> entry : orderRate.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            log.info("@@@@@@+"+key+"////"+value);
        }

        //제일 많이 팔린 상품
        Product bestseller = productOrderService.findBestSeller(id);
        model.addAttribute("bestseller",bestseller);

        //총매출액
        List<ProductOrder> order = productOrderService.getSellerMainPageOrderList(id);
        long totalsales = 0; // 예제 숫자 값
        for(ProductOrder o : order){
            long price = (long) (o.getOption_id().getProduct_id().getPrice() * o.getAmount());

            totalsales = totalsales + price;
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedSales = formatter.format(totalsales);
        model.addAttribute("totalsales",formattedSales);

        log.info("best : "+bestseller);
        
        return "seller/sellerMain";
    }

    //상품등록페이지
    @GetMapping("productRegistration.do")
    public String productRegistration(Authentication authentication,
                                      @AuthenticationPrincipal SecurityDetails securityDetails, Model model) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        return "seller/sellerProductWrite";
    }

    //상품등록
    @PostMapping("productSave.do")
    public String productSave(
            @ModelAttribute ProductDTO dto,
            @RequestParam("option_name") String[] option_names,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        String thumbnailUrl = null;
        List<String> fileUrls = new ArrayList<>();
        String category = null;


        if (!thumbnail.isEmpty()) {

            String thumbnailKey = s3Service.uploadFile(thumbnail, "thumbnails");
            thumbnailUrl = s3Service.getFileUrl(thumbnailKey);
            dto.setThumbnail_url(thumbnailUrl);


            Map<String, Object> rekognitionResult = rekognitionService.analyzeImageFromS3(thumbnailKey);
            if (rekognitionResult.containsKey("error")) {
                throw new IOException("Rekognition Error: " + rekognitionResult.get("error"));
            }


            String openAiUrl = "http://localhost:8082/categorize/ask";
            Map<String, Object> openAiBody = new HashMap<>();
            openAiBody.put("labels", rekognitionResult.get("labels"));

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> openAiResult = restTemplate.postForObject(openAiUrl, openAiBody, Map.class);
            if (openAiResult == null || openAiResult.containsKey("error")) {
                throw new IOException("OpenAI Error: " + openAiResult.get("error"));
            }

            category = (String) openAiResult.get("category");
        }


        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileKey = s3Service.uploadFile(file, "product-images");
                fileUrls.add(s3Service.getFileUrl(fileKey));
            }
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@: "+category);

        List<CategoryDetailDTO> categoryDetails = categoryDetailService.getAllCategoryDetails();
        for (CategoryDetailDTO categoryDetail : categoryDetails) {
            if (category.equals(categoryDetail.getCategory_detail_name())) {
                CategoryDetail matchingCategoryDetail = CategoryDetail.builder()
                        .id(categoryDetail.getId())
                        .category_id(categoryDetail.getCategory_id())
                        .category_detail_name(categoryDetail.getCategory_detail_name())
                        .build();
                dto.setCategory_detail_id(matchingCategoryDetail);
                break;
            }
        }


        dto.setCategory_id(dto.getSeller_id().getCategory_id());
        dto.setStatus(true);
        Product entity = productService.setProduct(dto);

        //옵션 등록
        for(String option_name : option_names) {
            ProductOption opEntity = productOptionService.save(option_name, entity);
        }

        if (!fileUrls.isEmpty()) {
            productPictureService.SetProductFiles(entity, fileUrls);
        }

        return "redirect:/seller/product.do";
    }

    //상품수정페이지
    @GetMapping("productUpdate.do")
    public String productUpdate(@RequestParam("id") Long id, Model model) {
        //RequestParam(id)는 product id

        // 1. 기존 상품 정보 조회
        Optional<Product> existingProduct = productService.existingProduct(id);

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        // 2. 기존 상품 데이터를 Model에 추가
        model.addAttribute("product", existingProduct.get());

        Product product = Product.builder()
                .id(id)
                .build();

        Optional<List<ProductOption>> option = productOptionService.findAllByProduct(product);
        model.addAttribute("optionList",option.get());

        Optional<List<ProductPicture>> productPicture = productPictureService.findAllByProduct(product);
        model.addAttribute("pictures",productPicture.get());


        // 4. 수정 페이지 반환
        return "seller/sellerProductModify";
    }

    //상품수정
    @PostMapping("productUpdate.do")
    public String productUpdateSave(
            @ModelAttribute ProductDTO dto,
            @RequestParam("option_name") String[] option_names,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("existingThumbnail") String existingThumbnail,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        // 파일 저장 경로 //로컬 경로라서 미사용
//        String uploadDir = "uploads/"; // 적절한 디렉토리 설정
//        File uploadDirFile = new File(uploadDir);
//        if (!uploadDirFile.exists()) {
//            uploadDirFile.mkdirs(); // 디렉토리가 없으면 생성
//        }


        // 썸네일 파일 업로드
        if (!thumbnail.isEmpty()) {
            String thumbnailKey = s3Service.uploadFile(thumbnail, "thumbnails");
            String thumbnailUrl = s3Service.getFileUrl(thumbnailKey);
            dto.setThumbnail_url(thumbnailUrl);

        }else{
            dto.setThumbnail_url(existingThumbnail);
        }

        // 상품 업데이트 메서드 호출
        productService.productUpdate(dto); // Product 객체 반환

        // 그 외 사진 업로드
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) { // 파일이 비어있지 않은 경우에만 처리
                productPictureService.filesDelete(dto.getId());


                String fileKey = s3Service.uploadFile(file, "product-images");
                filePaths.add(s3Service.getFileUrl(fileKey));


            }
        }

        // ProductPicture insert 메서드 호출
        Product entity = productService.DtoToEntity(dto);
        productPictureService.SetProductFiles(entity, filePaths);

        //option update
        List<String> optionList = new ArrayList<String>();
        if(option_names != null) {
            for(String option : option_names) {
                if(!option.equals("")) {
                    optionList.add(option);
                }
            }
        }

        productOptionService.optionUpdate(optionList,dto.getId());

        return "redirect:product.do";
    }

    //상품 리스트 페이지
    @GetMapping("product.do")
    public String productList(Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails, Model model, Criteria cri) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }
        //상품리스트
        cri.setAmount(12);
        List<ProductDTO> list = productService.getSellingList(cri, id);
        long totalCount = productService.getSellingListCount(cri,id);
        model.addAttribute("list", list);
        model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
        model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);


        return "seller/sellerSellingList";
    }

    //상품 공지 리스트
    @GetMapping("notice.do")
    public String noticeList(Authentication authentication,
                             @AuthenticationPrincipal SecurityDetails securityDetails, Model model, Criteria cri){
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        //공지 리스트
        cri.setAmount(10);
        List<ProductNotice> list = productNoticeService.getNoticeList(cri,id);
        long totalCount = productNoticeService.getNoticeCount(id);
        model.addAttribute("list", list);
        model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
        model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달

        return "seller/sellerNoticeList";
    }

    //상품공지등록페이지
    @GetMapping("noticeRegistration.do")
    public String noticeRegistration(Model model,Authentication authentication,
                                     @AuthenticationPrincipal SecurityDetails securityDetails) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        //등록된 상품 리스트
        List<Product> list = productService.getProductList(id);
        model.addAttribute("products",list);

        return "seller/sellerNoticeWrite";
    }

    //공지등록
    @PostMapping("noticeSave.do")
    public String noticeSave(@ModelAttribute ProductNoticeDTO dto){

        productNoticeService.setNotice(dto);

        return "redirect:/seller/notice.do";
    }

    //공지수정페이지
    @GetMapping("noticeUpdate.do")
    public String noticeUpdate(@RequestParam("id") Long id,Model model,@RequestParam("userId") Long userId) {
        //@RequestParam("id") notice id

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        Optional<ProductNotice> entity = productNoticeService.findById(id);
        model.addAttribute("notice", entity.get());

        //등록된 상품 리스트
        List<Product> list = productService.getProductList(userId);
        model.addAttribute("products",list);

        return "seller/sellerNoticeModify";
    }

    //공지수정
    @PostMapping("noticeUpdate.do")
    public String noticeUpdateSave(@ModelAttribute ProductNoticeDTO dto){

        productNoticeService.setNoticeUpdate(dto);

        return "redirect:/seller/notice.do";
    }

    //공지삭제
    @GetMapping("noticeDelete.do")
    public String noticeDelete(@RequestParam("id") Long id){
        //공지 id

        productNoticeService.noticeDelete(id);

        return "redirect:/seller/notice.do";
    }

    //판매내역
    @GetMapping("order.do")
    public String orderList(Authentication authentication,
                            @AuthenticationPrincipal SecurityDetails securityDetails,Model model,Criteria cri) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        cri.setAmount(10);
        List<ProductOrder> list = productOrderService.getSellerPageOrderList(cri,id);
        long totalCount = productOrderService.getSellerPageOrderCount(cri,id);
        model.addAttribute("list", list);
        model.addAttribute("pageMaker", new PageVo(cri, (int)totalCount));
        model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달

        return "seller/sellerOrderList";
    }

    //제목 ,상태 검색 페이징 비동기식
    @PostMapping("list_paging")//= seller Id
    public String list_paging(Criteria cri, Model model, Authentication authentication,
                              @AuthenticationPrincipal SecurityDetails securityDetails){

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            SecurityDetails userDetails = (SecurityDetails) securityDetails;
            long id = userDetails.getId();
            log.info(cri.getPageNum()+"page");
            log.info(cri.getOffset()+"offset");
            cri.setAmount(10);
            List<ProductOrder> list = productOrderService.getSellerPageOrderList(cri,id);
            long totalCount = productOrderService.getSellerPageOrderCount(cri,id);
            model.addAttribute("list", list);
            model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount));
            model.addAttribute("cri", cri); //추가: 뷰로 cri 객체 전달
        }

        return "fragments/sellerOrderList :: list";//공지사항 올린 shop 프로젝트 중 detail.html과 fragments/review 참고
    }

    //주문취소
    @GetMapping("orderCancellation.do")
    public String orderCancellation(@RequestParam("id") Long id){

        productOrderService.updateStatusToTrue(id);

        return "redirect:/seller/order.do";
    }

    //문의 받은 리스트
    @GetMapping("buyerQna.do")
    public String buyerQna(Model model,Authentication authentication,
                           @AuthenticationPrincipal SecurityDetails securityDetails) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        List<Question> list = questionService.getSellerPageBuyerQna(id);
        List<Answer> aList = answerService.getSellerPageBuyerQna(id);
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

        // 모든 status가 false가 아닌지 확인
        boolean allStatusNonZero = list.stream().allMatch(Question::isStatus);
        model.addAttribute("allStatusNonZero", allStatusNonZero);

        return "seller/sellerBuyerQnaList";
    }

    @PostMapping("answer.do")
    public String answer(@ModelAttribute AnswerDTO dto){

        answerService.save(dto);
        questionService.updateStatusTo1(dto.getQuestion_id());


        return "redirect:/seller/buyerQna.do";
    }

    //문의 한 리스트
    @GetMapping("myQna.do")
    public String myQna(Model model,Authentication authentication,
                        @AuthenticationPrincipal SecurityDetails securityDetails) {
        Long id= 0L;
        //user id값
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && (principal instanceof SecurityDetails)){
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id",userDetails.getId());
            id = userDetails.getId();
        }

        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        List<Question> list = questionService.getSellerPageMyQna(id);
        List<Answer> aList = answerService.getSellerPageMyQna(id);
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

        boolean allStatusNonZero = list.stream().allMatch(Question::isStatus);
        model.addAttribute("allStatusNonZero", allStatusNonZero);

        return "seller/sellerMyQnaList";
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
        return "seller/sellerPwChk";
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
            if (sellerService.verifyPassword(id, password)) {
                session.setAttribute("pw_checked", 33);//세션 등록
                return "true";
            }else{
                return "false";
            }
        }
        return "error";
    }

    //판매자 정보 페이지
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
            if(session.getAttribute("pw_checked") != null){
                int pwChk = (int) session.getAttribute("pw_checked");//password 체크했는지 확인

                if (pwChk == 33) {
                    Optional<Seller> seller = sellerService.findById(id);
                    model.addAttribute("seller", seller.get());
                    return "seller/sellerInformation";
                }
            }

        }
        return "redirect:/seller/password.do";
    }
    //판매자 정보 수정
    @PostMapping("informationModify.do")
    @ResponseBody
    public String informationModify(SellerDTO dto, @RequestParam(name="passwordChk", required = false) String passwordChk,
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
                        sellerService.sellerUpdate(dto);
                        return "true";
                    } else {
                        return "notEqual";
                    }
                }
                Optional<Seller> seller = sellerService.findById(dto.getId());
                dto.setPassword(seller.get().getPassword());
                sellerService.sellerUpdate(dto);
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

        sellerService.withdrawal(id);

        return "redirect:/logout.do";
    }

    //상품삭제
    @PostMapping("delete.do")
    public String productDelete(@RequestParam("id") Long id) {
        //상품 id


        //상품 삭제지만 status 0(재고 없음)으로 바꾸는 거임
        productService.updateStatusById(id);

        return "redirect:seller/sellerSellingList";
    }


}
