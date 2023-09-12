package com.pet.sitter;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private MemberRepository memberRepository;
    //요청주소 http://localhost:8090/
    //메인화면이 보여줘야한다.
    @GetMapping("/main")
    public String index() {

        return "redirect:/mainboard/list"; // 사용자 정보를 포함한 메인 페이지로 이동
    }
}