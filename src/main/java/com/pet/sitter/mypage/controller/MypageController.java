package com.pet.sitter.mypage.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;


    @GetMapping("/info/{naya}")
    public String mypageinfo(@PathVariable("naya") String memberId, Model model){
        MemberDTO memberDTO = mypageService.getMember(memberId);
        model.addAttribute("memberDTO",memberDTO);
        return "myInfo";
    }
    @GetMapping("/myArticleList/{xiuxiu}")
    public String myArticle(@PathVariable("xiuxiu") String id,
                            @RequestParam(value="page",defaultValue="0") int page, Model model){
        Page<PetSitterDTO> petsitterPage= this.mypageService.getMyArticleList(id,page);

        model.addAttribute("petsitterPage",petsitterPage);
        return "mypage/myArticleList";
    }
}
