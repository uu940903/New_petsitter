package com.pet.sitter.qna.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.service.AnswerService;
import com.pet.sitter.qna.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
    public  String addAnswer(@PathVariable Long qnaNo, @RequestParam String content){
        QuestionDTO questionDTO = questionService.detail(qnaNo);
        Member member = new Member();
        member.setId(6L);

        answerService.addAnswer(questionDTO,member,content);
        return "redirect:/question/detail/" + qnaNo;
    }

  /*  //답변수정폼요청
    @GetMapping("/modify/{id}")
    public String answerModify(@PathVariable("id") Long id, AnswerForm answerForm){
        AnswerDTO answerDTO = answerService.detail(id);
        answerForm.getContent(answerDTO.getContent());
        return "AnswerForm";
    }
*/
}