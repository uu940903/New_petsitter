package com.pet.sitter.mainboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.service.MainBoardService;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/mainboard")
@Controller
public class MainBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final MainBoardService mainBoardService;
    public final MemberService memberService;


    /*
    @GetMapping("/list")
    public String getList(Model model) {
        List<PetSitterDTO> petSitterList = mainBoardService.getList();
        model.addAttribute("petSitterList", petSitterList);
        System.out.println("list 크기"+petSitterList.size());
        return "mainboard/list";
    }
    */

    //페이지네이션
    @GetMapping("/list")
    public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PetSitterDTO> petSitterPage = mainBoardService.getList(page);
        petSitterPage.stream().toList();
        model.addAttribute("petSitterPage", petSitterPage);
        return "mainboard/list";
    }

    /*
    무한 스크롤
    @GetMapping("/api/list")
    public String getListInfinite(Model model, @RequestParam("sitterNo") Long lastSitterNo, @RequestParam int size) {
        if(lastSitterNo==0) {
            lastSitterNo= 6L;
        }
        List<PetSitterDTO> petsitterList = mainBoardService.getListInfinite(lastSitterNo, size);
        model.addAttribute("petsitterList", petsitterList);
        return "mainboard/list";
    }
    */

    @GetMapping("/detail/{sitterNo}")
    public String getDetail(Model model, @PathVariable("sitterNo") Long no) {
        PetSitterDTO petSitter = mainBoardService.getDetail(no);
        model.addAttribute("petSitter", petSitter);
        return "mainboard/detail";
    }


    //search
    @PostMapping("/search")
    public ResponseEntity<String> searchList(Model model, @RequestParam Map<String, Object> map, @RequestParam(value = "page", defaultValue = "0") int page) {
        String category = (String) map.get("category");
        String petCategory = (String) map.get("petCategory");
        String petAddress = (String) map.get("address");
        String day = (String) map.get("day");
        String timeStr = (String) map.get("time");

        Page<PetSitterDTO> petSitterDTOPage = mainBoardService.searchList(page, category, petCategory, petAddress, day, timeStr);
        List<PetSitterDTO> petSitterDTOList = petSitterDTOPage.getContent();
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(petSitterDTOList);
        } catch (JsonProcessingException e) {
            // JSON 직렬화 중 오류가 발생한 경우 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 변환 오류");
        }
        // JSON 데이터를 AJAX 응답으로 반환합니다.
        return ResponseEntity.ok(json);
    }


    //********************************혜지시작

    //글등록 폼 요청
    //!!!!!스프링시큐리티 적용되면 @PreAuthorize("isAuthenticated()") //로그인했니? 추가해야됨
    //!!!!@Vaild 추가해야됨
    @GetMapping("/write")
    public String writeForm() {
        logger.info("MainBoardController-writeForm()진입");
        return "mainboard/writeForm";
    }


    //글등록 처리
    @PreAuthorize("isAuthenticated()") //로그인했니?
    @PostMapping("/write")
    public String write(@Valid WriteForm writeForm, BindingResult bindingResult,
                        PetSitterDTO petSitterDTO, Principal principal, MultipartFile[] boardFile) throws IOException {
        logger.info("MainBoardController-write()진입");
        String id = principal.getName();

        if (bindingResult.hasErrors()) {
            return "mainboard/writeForm";
        }

        mainBoardService.write(petSitterDTO, id, boardFile);


        return String.format("redirect:/mainboard/list");
    }
}