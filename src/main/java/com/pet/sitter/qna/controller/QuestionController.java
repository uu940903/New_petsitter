package com.pet.sitter.qna.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public String write(QuestionDTO questionDTO,@RequestParam("file") MultipartFile[] file,Model model) throws IOException {
        Member member = new Member();
        member.setId(6L);

        try{
            questionService.savePost(questionDTO, file);
            return "redirect:/question/list";
        }catch (IOException e) {
            // 파일 업로드 중 오류가 발생한 경우에 대한 예외 처리
            e.printStackTrace();
            model.addAttribute("errorMessage", "글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }

//        Member member = questionDTO.getMember();
//        if(member != null){
//            member.getId();
//        }
//
//        questionService.savePost(questionDTO);
//        return "redirect:/question/list";
//    }

    //question list 조회
    @GetMapping("/list")
    public String list(Model model,@RequestParam(value = "page", defaultValue = "0") int page){
        Page<Question> questionPage = questionService.questionList(page);
        model.addAttribute("questionPage",questionPage);
        return "qna/QuestionList";

//        List<QuestionDTO> questionList = questionService.questionList();
    }

    //question 상세 조회
    @GetMapping("/detail/{qnaNo}")
    public String detail(@PathVariable Long qnaNo, Model model){
        QuestionDTO questionDTO = questionService.detail(qnaNo);
        model.addAttribute("questionDTO",questionDTO);

        return "qna/QuestionDetail";
    }

    //question 게시글 수정폼
    @GetMapping("/edit/{qnaNo}")
    public String edit(Model model,@PathVariable Long qnaNo){
        QuestionDTO questionDTO = questionService.detail(qnaNo);

        model.addAttribute("questionDTO",questionDTO);
        return "qna/QuestionUpdate";
    }

    //question 게시글 수정
    @PutMapping ("/qnaUpdate")
    public String update(QuestionDTO questionDTO, Model model, Long qnaNo){

        questionService.update(qnaNo,questionDTO);
        model.addAttribute("questionDTO",questionDTO);
        return "redirect:/question/list";

    }

    //question 게시글 삭제
    @DeleteMapping("/delete/{qnaNo}")
    public String delete(@PathVariable Long qnaNo) {
        questionService.delete(qnaNo);
        return "redirect:/question/list";
    }

}
