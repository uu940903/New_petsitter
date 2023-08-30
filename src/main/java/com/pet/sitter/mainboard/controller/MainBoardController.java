package com.pet.sitter.mainboard.controller;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.service.MainBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@RequestMapping("/mainboard")
@Controller
public class MainBoardController {

    @Autowired
    public MainBoardService mainBoardService;

 /*   @GetMapping("/list")
    public String getList(Model model) {
        List<Petsitter> petSitterList = mainBoardService.getList();
        System.out.println("더미 데이터 개수"+petSitterList.size());
        model.addAttribute("petSitterList", petSitterList);
        return "mainboard/list";
    }
*/

    @GetMapping("/list")
    public String getList(Model model) {
        List<PetSitterDTO> petSitterList = mainBoardService.getList();
        System.out.println("더미 데이터 개수"+petSitterList.size());
        model.addAttribute("petSitterList", petSitterList);
        return "mainboard/list";
    }
}
