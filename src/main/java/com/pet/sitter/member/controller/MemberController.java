package com.pet.sitter.member.controller;

import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String login(MemberDTO memberDTO, Model model, HttpSession session) {

        System.out.println(memberDTO.getId()+", "+memberDTO.getPw());
        boolean loginSuccess = memberService.login(memberDTO);

        if (loginSuccess) {
            MemberDTO loginMember = memberService.getMember(memberDTO.getId());
            session.setAttribute("userID",loginMember.getId());
            session.setAttribute("name",loginMember.getName());
            session.setAttribute("nickname",loginMember.getNickname());
            return "redirect:/main"; // 로그인 후 이동할 페이지 설정
        } else {
            model.addAttribute("error", "Invalid ID or password");
            return "/member/login";
        }
    }
@PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/member/login";
    }


}