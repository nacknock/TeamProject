package com.example.HamuPochi.Controller;

import com.example.HamuPochi.DTO.BuyerDTO;
import com.example.HamuPochi.DTO.SellerDTO;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Entity.Category;
import com.example.HamuPochi.Entity.Seller;
import com.example.HamuPochi.Service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sign/*")
@Log4j2
@RequiredArgsConstructor
public class SignController {

    String checkIncode;

    @Autowired
    private final BuyerService buyerService;

    @Autowired
    private final SellerService sellerService;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final MailService mailService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @GetMapping("login.do")
    public String login(Model model,@RequestParam(value = "errorMessage",required = false, defaultValue = "0") int errorMessage,
                        @RequestParam(value = "ban",required = false, defaultValue = "") String ban){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("errorMessage",errorMessage);
        if(ban.equals("")){
            model.addAttribute("ban",null);
        }else{
            LocalDateTime dateTime = LocalDateTime.parse(ban);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            ban = dateTime.format(formatter);
            model.addAttribute("ban",ban);
        }
        return "sign/login";
    }

    //회원가입 유형 선택 페이지 get
    @GetMapping("joinSelect.do")
    public String joinSelect(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/joinSelect";
    }

    //판매자 회원가입 페이지 get
    @GetMapping("joinSeller.do")
    public String joinSeller(Model model){
        //select * from category / header 카테고리 + 판매자 업종
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/joinSeller";
    }

    //구매자 회원가입 페이지 get
    @GetMapping("joinBuyer.do")
    public String joinBuyer(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/joinBuyer";}

    @GetMapping("mail.do")//회원가입시 인증용
    @ResponseBody // json 형식으로 값을 되돌려 줘라
    public String mailSend(@RequestParam("email") String email) throws MessagingException {
        log.info("@@@@@@@@@컨트롤러 이메일:" +email);
        if(!sellerService.CheckByEmail(email)){
            if(!buyerService.CheckByEmail(email)){
                checkIncode = mailService.createMailForSellerJoin(email);
                return checkIncode;
            }
        }
        return "false";
    }

    @PostMapping("sellerJoinAction.do")
    @ResponseBody // json 형식으로 값을 되돌려 줘라
    public Map<String, String> sellerJoinAction(@ModelAttribute SellerDTO sellerDTO, Model model) {
        log.info("@@@@@@@@@@@@@@@@@@Join start@@@@@@@@@@@@@@@@@@");
        log.info("@@@@@@@@@@@@@@@@@@"+sellerDTO+"@@@@@@@@@@@@@@@@@@");
        String encodePwd = passwordEncoder.encode(sellerDTO.getPassword());
        sellerDTO.setPassword(encodePwd);
        Map<String, String> result = new HashMap<>();
        if(sellerService.save(sellerDTO)) {
            result.put("result", "ok");
        }else {
            result.put("result", "notok");
        }

        return result;
    }

    @PostMapping("buyerJoinAction.do")
    @ResponseBody // json 형식으로 값을 되돌려 줘라
    public Map<String, String> buyerJoinAction(@ModelAttribute BuyerDTO buyerDTO, Model model) {
        log.info("@@@@@@@@@@@@@@@@@@Join start@@@@@@@@@@@@@@@@@@");
        log.info("@@@@@@@@@@@@@@@@@@"+buyerDTO+"@@@@@@@@@@@@@@@@@@");
        String encodePwd = passwordEncoder.encode(buyerDTO.getPassword());
        buyerDTO.setPassword(encodePwd);
        Map<String, String> result = new HashMap<>();
        if(buyerService.save(buyerDTO)) {
            result.put("result", "ok");
        }else {
            result.put("result", "notok");
        }

        return result;
    }

    @GetMapping("emailFindSelect.do") //emailFindSelect 페이지 이동
    public String emailFindSelect(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/emailFindSelect";
    }

    @GetMapping("buyerEmailFind.do") //buyerEmailFind 페이지 이동
    public String buyerEmailFind(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/buyerEmailFind";
    }

    @GetMapping("sellerEmailFind.do") //sellerEmailFind 페이지 이동
    public String sellerEmailFind(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/sellerEmailFind";
    }
    
    @PostMapping("buyerEmailFindAction.do")//email을 확인하고 비동기로 페이지에 결과 보내주기
    @ResponseBody
    public String buyerEmailFindAction(Model model,BuyerDTO buyerDTO) throws MessagingException {

        String result = "false";

        Buyer entity = buyerService.checkByDTO(buyerDTO); // 유저가 입력한 정보와 일치하는 이메일 찾기

        if(entity != null){ //entity.getEmail() = 유저가 찾는 email//buyerDTO.getEmail() = 메일을 보낼 이메일
            result = mailService.createMailForEmailFind(entity.getEmail(),buyerDTO.getEmail());
            return result;//이메일 발송 성공하면 true;
        }

        return "false";
    }

    @PostMapping("sellerEmailFindAction.do")//email을 확인하고 비동기로 페이지에 결과 보내주기
    @ResponseBody
    public String sellerEmailFindAction(Model model,SellerDTO sellerDTO) throws MessagingException {

        String result = "error";

        log.info("check:"+sellerDTO);

        Seller entity = sellerService.checkByDTO(sellerDTO);

        if(entity != null){ //entity.getEmail() = 유저가 찾는 email//buyerDTO.getEmail() = 메일을 보낼 이메일
            //이메일 발송 성공하면 true;
            result = mailService.createMailForEmailFind(entity.getEmail(),sellerDTO.getEmail());

        }else{
            result = "false";
        }

        return result;
    }

    @GetMapping("pwFind.do") //pwFind 페이지 이동
    public String pwFind(Model model){
        //select * from category / header 카테고리
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "sign/pwFind";
    }

    @GetMapping("pwFindEmail.do")//비밀번호 찾기 시 인증용
    @ResponseBody // json 형식으로 값을 되돌려 줘라
    public String pwFindEmail(@RequestParam("email") String email) throws MessagingException {
        log.info("@@@@@@@@@컨트롤러 이메일:" +email);
        if(sellerService.CheckByEmail(email)){//seller인 경우
            checkIncode = mailService.createMailForFindPw(email);
            return checkIncode;
        }else if(buyerService.CheckByEmail(email)){//buyer인 경우
            checkIncode = mailService.createMailForFindPw(email);
            return checkIncode;
        }
        
        return "false";
    }

    @PostMapping("pwFindAction.do")//비동기
    @ResponseBody
    public String pwFindAction(@RequestParam("email") String email,
                               @RequestParam("newPw") String newPw){

        String encodePwd = passwordEncoder.encode(newPw);

        if(sellerService.CheckByEmail(email)){//seller인 경우

            Seller seller = sellerService.findByEmail(email); //email 확인
            if(seller != null){
                sellerService.updatePw(seller,encodePwd);
                return "true";
            }

        }else if (buyerService.CheckByEmail(email)) { // Buyer인 경우
            Buyer buyer = buyerService.findByEmail(email).orElse(null); // email 확인

            if (buyer != null) {
                buyerService.updatePw(buyer, encodePwd);
                return "true";
            }
        }
        return "idNotFound";//seller에서도 buyer에서도 id를 못찾은 경우
    }

    @PostMapping("googleLogin")
    @ResponseBody
    public Map<String, String> googleLogin(@RequestParam("email") String email,
                                           @RequestParam("name") String name){

        Map<String, String> result = new HashMap<>();

        if (buyerService.existsByEmail(email)) {
            result.put("result", "exists");
            return result;
        }

        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setEmail(email);
        buyerDTO.setName(name);
        buyerDTO.setNickname(name);
        String defaultPassword = "guguruUsEr";
        buyerDTO.setPassword(passwordEncoder.encode(defaultPassword));

        if (buyerService.save(buyerDTO)) {
            result.put("result", "ok");
        } else {
            result.put("result", "notok");
        }

        return result;
    }


}
