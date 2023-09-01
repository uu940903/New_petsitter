package com.pet.sitter.member.controller;

import com.pet.sitter.member.service.MemberService;
import com.pet.sitter.member.validation.UserCreateForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "/member/login"; //templates 폴더 하위의 login_form.html문서를 의미
    }

    //회원가입폼을 보여줘 요청
    @GetMapping("/join")
    public String signup(UserCreateForm userCreateForm) {
        return "/member/join";
    }

    @PostMapping("/join")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        System.out.println("id : "+userCreateForm.getMemberId());
        if(bindingResult.hasErrors()) { //에러가 존재하면 signup을 보여줘
            return "/member/join";
        }
        //2.비즈니스로직처리
        //비밀번호와 비밀번호 확인을 서로 비교하여 불일치하면 signup_form.html문서로 이동
        if(!userCreateForm.getPw1().equals(userCreateForm.getPw2())) {
            bindingResult.rejectValue("password2","passwordInCorrect","비밀번호 입력이 일치하지 않습니다.");
            return "/member/join";

        }
        try {
            memberService.create(userCreateForm);
        }catch (DataIntegrityViolationException e) { // 여기에서는 username(회원id은 uk, email은 uk)->제약조건에 걸리면 발생
            e.printStackTrace();
            bindingResult.reject("signupFailed","이미 등록된 회원이 있습니다.");
            return "/member/join";
        }catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/member/join";
        }
        //3.Model
        //4.View
        return "redirect:/main"; //회원가입 성공 시 메인화면으로 이동
    }
}
