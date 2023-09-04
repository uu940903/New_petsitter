/*

package com.pet.sitter.member.controller;

import com.pet.sitter.common.entity.User;
import com.pet.sitter.member.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/loginForm")
    public String home() {
        return "/member/loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm() {
        return "/member/joinForm";
    }
    @PostMapping("/member/joinForm")
    public String join(User user) {
        oAuthService.joinMember(user);
        return "redirect:/member/loginForm";
    }
}*/
