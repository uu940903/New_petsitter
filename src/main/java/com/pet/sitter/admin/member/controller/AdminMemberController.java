package com.pet.sitter.admin.member.controller;

import com.pet.sitter.admin.member.service.AdminMemberService;
import com.pet.sitter.admin.member.validation.AdminMemberForm;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.dto.MemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    //회원리스트
    @GetMapping("/memberList")
    public String memberList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             Pageable pageable) {
        Page<MemberDTO> memberPage = adminMemberService.getMemberList(page);
        model.addAttribute("memberPage", memberPage);
        return "admin/memberList";
    }

    //회원상세보기
    @GetMapping("/memberDetail/{id}")
    public String getMemberDetail(@PathVariable("id") Long id,
                                  Model model, AdminMemberForm AdminMemberForm) {
        MemberDTO memberDetail = adminMemberService.getMemberDetail(id);
        model.addAttribute("memberDetail", memberDetail);
        return "admin/memberDetail";
    }

    //수정폼
    @GetMapping("/memberModify/{id}")
    public String showModifyForm(AdminMemberForm adminMemberForm,
                                 @PathVariable("id") Long id, Principal principal, Model model) {
        MemberDTO memberDTO = adminMemberService.getMemberDetail(id);
        adminMemberForm.setMemberId(memberDTO.getMemberId());
        adminMemberForm.setName(memberDTO.getName());
        adminMemberForm.setNickname(memberDTO.getNickname());
        adminMemberForm.setAddress(memberDTO.getAddress());
        adminMemberForm.setBirth(memberDTO.getBirth());
        adminMemberForm.setPhone(memberDTO.getPhone());
        adminMemberForm.setEMail(memberDTO.getEMail());
        System.out.println("id = " + id);

        model.addAttribute("memberDTO", memberDTO);
        return "admin/memberForm"; // Return the form view
    }

    //수정처리
    @PostMapping("/memberModify/{id}")
    public String modify(@Valid AdminMemberForm adminMemberForm, BindingResult bindingResult,
                         @PathVariable("id") Long id, Model model, Principal principal) {
        System.out.println("수정 modify 진입");
        MemberDTO memberDTO = adminMemberService.getMemberDetail(id);
        System.out.println("에러 ="+bindingResult.hasErrors());
        System.out.println("에러 ="+bindingResult.getErrorCount());
        //1.파라미터받기

        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDTO", memberDTO);
            return "admin/memberForm";
        }

        System.out.println("전화 = "+adminMemberForm.getPhone());
        System.out.println("닉네임 = "+adminMemberForm.getNickname());
        //2.비즈니스로직수행
        Member member = adminMemberService.getModify(id); //질문상세
        adminMemberService.modify(id,adminMemberForm.getMemberId(), adminMemberForm.getName(),adminMemberForm.getNickname(),
                adminMemberForm.getBirth(), adminMemberForm.getPhone(),
                adminMemberForm.getAddress(),adminMemberForm.getEMail());
        return String.format("redirect:/admin/memberDetail/%d", id); //수정 상세페이지로 이동
    }

    //삭제
    @GetMapping("/memberDelete/{id}")
    public String memberDelete(@PathVariable("id") Long id, Principal principal) {
        adminMemberService.delete(id);
        return "redirect:/admin/memberList";
    }



}
