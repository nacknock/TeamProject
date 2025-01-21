package com.example.HamuPochi.Controller;

import com.example.HamuPochi.Config.SecurityDetails;
import com.example.HamuPochi.DTO.*;
import com.example.HamuPochi.Entity.Answer;
import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Service.AnswerService;
import com.example.HamuPochi.Service.QuestionService;
import com.example.HamuPochi.Util.Criteria;
import com.example.HamuPochi.Util.PageVo;
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


@Log4j2
@Controller
@RequestMapping("/admin/qna/*")
@RequiredArgsConstructor
public class AdminQuestionController {


    @Autowired
    private final QuestionService questionService;
    @Autowired
    private final AnswerService answerService;

    @GetMapping("list.do") // 문의 목록 조회 요청 처리
    public String questionList(Model model, Criteria cri) {
        cri.setAmount(5); // 한 페이지에 보여줄 문의 수 설정

        // 문의 리스트 조회
        List<Question> questionList = questionService.getAdminQuestionList(cri);
        long totalCount = questionService.getAdminQuestionCount(cri);

        for(Question question : questionList) {
            System.out.println(question.getTitle()+"답변 여부"+question.isStatus());
        }
        // 모델에 데이터 추가
        model.addAttribute("questionList", questionList); // 문의 리스트
        model.addAttribute("pageMaker", new PageVo(cri, (int) totalCount)); // 페이지 정보
        model.addAttribute("cri", cri); // 검색 조건 정보
        model.addAttribute("totalCount", totalCount); // 전체 문의 글 수

        return "Admin/qna_list"; // 뷰 이름 반환
    }

    @GetMapping("detail.do")
    public String question_detail(Authentication authentication,
                                  @AuthenticationPrincipal SecurityDetails securityDetails,
                                  @RequestParam("id") Long id, Model model) {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null && (principal instanceof SecurityDetails)) {
            }
            SecurityDetails userDetails = (SecurityDetails) securityDetails;

            model.addAttribute("id", userDetails.getId());

            Optional<Question> question = questionService.findOneById(id);//Question 불러오기
            Optional<Answer> opAnswer = answerService.findOneByQuestionId(id);//Answer 불러오기

            model.addAttribute("question", question.get());
            if (opAnswer.isPresent()) {
                model.addAttribute("answer", opAnswer.get());
            }

            return "Admin/qna_detail";

        }


        return "Admin/qna_list";

    }

    @PostMapping("save.do")
    public String question_insert(@ModelAttribute AnswerDTO dto, Model model) {

        answerService.save(dto); //answer 저장
        //question status 1로 변경 //question status 0=답변X 1=답변O
        questionService.updateStatusTo1(dto.getQuestion_id());

        return "redirect:/admin/qna/list.do";

    }
}
