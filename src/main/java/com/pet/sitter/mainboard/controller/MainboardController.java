package com.pet.sitter.mainboard.controller;

import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.service.MainboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mainboard")
public class MainboardController {

    private final MainboardService mainboardService;

    //글작성 폼 요청
    @GetMapping ("/write")
    public String writeForm () {
      return "mainboard/writeForm";
    }

    @PostMapping("/write")
    public String write (PetSitterDTO petSitterDTO) {
        //파라미터 받기
        //비즈니스로직
        mainboardService.write(petSitterDTO);
        //Model
        //View
        return "list";
    }
}
