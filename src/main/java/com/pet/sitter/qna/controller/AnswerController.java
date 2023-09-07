package com.pet.sitter.qna.controller;

import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.AnswerDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.service.AnswerService;
import com.pet.sitter.qna.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    public AnswerController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }


    //답변 등록처리
    @PostMapping("/add/{qnaNo}")
    public String addAnswer(@PathVariable Long qnaNo, AnswerDTO answerDTO){
        QuestionDTO questionDTO = questionService.detail(qnaNo);
        if (questionDTO != null) {
            answerService.addAnswer(questionDTO,answerDTO);
        }
        return "redirect:/question/detail/" + qnaNo;
    }

   //답변수정폼요청
    @GetMapping("/modify/{id}")
    public String detail(@PathVariable("id") Long id, Member member, QuestionDTO questionDTO,Model model){
        AnswerDTO answerDTO = answerService.getDetail(id);
        model.addAttribute("answerDTO",answerDTO);
        return "qna/QuestionUpdate";
    }
    
    //답변 리스트
    @GetMapping("/detail/{qnaNo}")
    public String detail(@PathVariable("qnaNo") Long qnaNo,@PathVariable("answerId") Long id, Model model) {
        QuestionDTO questionDTO = questionService.detail(qnaNo);
        AnswerDTO answerDTO = answerService.getDetail(id);

        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("answerDTO", answerDTO);

        return "qna/QuestionDetail";
    }

}