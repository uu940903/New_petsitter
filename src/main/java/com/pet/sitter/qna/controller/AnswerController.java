package com.pet.sitter.qna.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.service.MemberService;
import com.pet.sitter.qna.dto.AnswerDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.service.AnswerService;
import com.pet.sitter.qna.service.QuestionService;
import com.pet.sitter.qna.validation.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;


@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final MemberService memberService;

    //답변 등록처리
    @PostMapping("/add/{qnaNo}")
    public String addAnswer(@PathVariable Long qnaNo, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal, Model model) {
        QuestionDTO questionDTO = questionService.detail(qnaNo);

        if (bindingResult.hasErrors()) {      //유효성 검사를 통과하지 못하여 에러가 존재하면
            model.addAttribute("questionDTO", questionDTO);
            return "qna/QuestionDetail";       //question_detail.html 이동
        }
        Member member = memberService.getUser(principal.getName());
        answerService.addAnswer(questionDTO, answerForm, member);
        model.addAttribute("answerForm", new AnswerForm());
        return String.format("redirect:/question/detail/%s", qnaNo);
    }


    //답변 리스트
    @GetMapping("/modify/{id}")
    public String detail(@PathVariable("id") Long id, Principal principal, AnswerForm answerForm,Model model) {
        AnswerDTO answerDTO = answerService.getDetail(id);

        if(!answerDTO.getMember().getMemberId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        answerForm.setContent(answerDTO.getContent());
        answerForm.setId(answerDTO.getId());
        answerForm.setCreatedDate(answerDTO.getCreatedDate());

        model.addAttribute("answerForm",answerForm);
        return "qna/AnswerForm";
    }

    //답변수정
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Principal principal, @Valid AnswerForm answerForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){      //유효성 검사시 에러가 발생하면
            return "qna/AnswerForm";
        }
        //수정을 위한 답변 상세 조회
        AnswerDTO answerDTO = answerService.getDetail(id);
        if(!answerDTO.getMember().getMemberId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        answerService.modify(id, answerDTO,answerForm);
        return String.format("redirect:/question/detail/%d",answerDTO.getQuestion().getQnaNo());
    }

    //답변 삭제
   @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Long id,Principal principal){
       AnswerDTO answerDTO = answerService.getDetail(id);
       if(!answerDTO.getMember().getMemberId().equals(principal.getName())){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
       }
       answerService.answerDelete(id);
       return String.format("redirect:/question/detail/%d",answerDTO.getQuestion().getQnaNo());
   }

}
