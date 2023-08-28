package com.pet.sitter;

import com.pet.sitter.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private MemberRepository memberRepository;
    //요청주소 http://localhost:8090/
    //메인화면이 보여줘야한다.
    //일단 여기에서는 질의목록페이지(question.html)출력
    @GetMapping("/main")
//    @ResponseBody
    public String index(Model model){
        //질문목록조회 : findAll()
        return "/main"; // templates폴더하위 main.html문서
    }
}