package com.pet.sitter.qna.controller;

import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    //question 글 작성 폼 보여주기
    @GetMapping("/write")
    public String writeForm(){
        return "qna/questionForm";
    }


    //quesiton 글 작성
    @PostMapping("/write")
    public String write(QuestionDTO questionDTO,Model model){
        questionService.savePost(questionDTO);
        return "redirect:/qna/list";
    }

    //question list 조회
    @GetMapping("/list")
    public String list(Model model){
        List<QuestionDTO> questionList = questionService.questionList();
        model.addAttribute("questionList",questionList);
        return "qna/QuestionList";
    }

}
