package com.pet.sitter.mainboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import com.pet.sitter.mainboard.service.MainBoardService;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/mainboard")
@Controller
public class MainBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainBoardService mainBoardService;

    @Autowired
    public MemberService memberService;


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
    //AreaSearch 테이블에 insert


    //글등록 폼 요청
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String writeForm(WriteForm writeForm) {
        logger.info("MainBoardController-writeForm()진입");
        return "mainboard/writeForm";
    }


    /*//글등록 처리
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
    }*/




    // 글등록 처리
    @PreAuthorize("isAuthenticated()") //로그인했니?
    @PostMapping("/write")
    public String write(@Valid WriteForm writeForm,
                        BindingResult bindingResult,
                        Principal principal,
                        MultipartFile[] boardFile) throws IOException {
        logger.info("MainBoardController-write()진입");

        String id = principal.getName();

        if (bindingResult.hasErrors()) {
            return "mainboard/writeForm";
        }


        // WriteForm을 PetSitterDTO로 변환
        PetSitterDTO petSitterDTO = writeForm.convertToPetSitterDTO();

        mainBoardService.write(petSitterDTO, id, boardFile);

        return "redirect:/mainboard/list";
    }




    //게시글 수정 폼 요청
    @GetMapping("/modify/{sitterNo}")
    public String updateForm(@PathVariable Long sitterNo, Principal principal,
                             Model model, WriteForm writeForm) {
        logger.info("MainBoardController-updateForm()진입");

        PetSitterDTO petSitterDTO = mainBoardService.getDetail(sitterNo);

        // 글 작성자와 로그인한 유저가 동일하지 않으면 수정 권한이 없음
        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        writeForm.setPetTitle(petSitterDTO.getPetTitle());
        writeForm.setPetContent(petSitterDTO.getPetContent());
        writeForm.setCategory(petSitterDTO.getCategory());
        writeForm.setPetCategory(petSitterDTO.getPetCategory());
        writeForm.setPrice(petSitterDTO.getPrice());
        writeForm.setStartTime(petSitterDTO.getStartTime());
        writeForm.setEndTime(petSitterDTO.getEndTime());
        writeForm.setPetAddress(petSitterDTO.getPetAddress());

        model.addAttribute("writeForm", writeForm);
        return "mainboard/updateForm";
    }


    //게시글 수정 처리
    @PostMapping ("/modify/{sitterNo}")
    public String update (@PathVariable Long sitterNo, @Valid WriteForm writeForm, BindingResult bindingResult,
                          Principal principal, MultipartFile[] boardFile, String id) throws IOException {
        logger.info("MainBoardController-update() 진입");

        id = principal.getName();

        if (bindingResult.hasErrors()) {
            return "mainboard/editForm";
        }

        mainBoardService.update(writeForm, sitterNo,boardFile, id);

        return String.format("redirect:/mainboard/detail/%s", + sitterNo);

    }


    //게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{sitterNo}")
    public String delete(@PathVariable Long sitterNo, Principal principal) {
        logger.info("MainBoardController-delete() 진입");

        String id = principal.getName();

        mainBoardService.delete(sitterNo, id);

        return "redirect:/mainboard/list";
    }
























    /*//게시글 수정 폼 요청
    @GetMapping("/modify/{sitterNo}")
    public String updatePetsitterForm(@PathVariable Long sitterNo, Principal principal,
                                      WriteForm writeForm, PetSitterFileDTO petSitterFileDTO) {

        //글 번호 들고오기
        PetSitterDTO petSitterDTO = mainBoardService.updateForm(sitterNo);

        //글작성자와 로그인한 유저가 동일하지 않으면 Exception
        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        petSitterDTO.getFileDTOList();
        //petSitterDTO.getFileDTOList().get().setSitterNo();

      *//*  for (PetSitterFileDTO sitterNoForFile : petSitterDTO.getFileDTOList()) {
            logger.info("컨트롤러에서 시터엔오포파일 확인: {}", sitterNoForFile.getSitterNo());
        }*//*

        String id = petSitterDTO.getMember().getMemberId();
        logger.info("id={}", id);


        //원글 정보 들고오기
        petSitterDTO.getMember().setMemberId(id);
        writeForm.setPetTitle(petSitterDTO.getPetTitle());
        writeForm.setPetContent(petSitterDTO.getPetContent());
        writeForm.setCategory(petSitterDTO.getCategory());
        writeForm.setPetCategory(petSitterDTO.getPetCategory());
        writeForm.setPrice(petSitterDTO.getPrice());
        writeForm.setStartTime(petSitterDTO.getStartTime());
        writeForm.setEndTime(petSitterDTO.getEndTime());
        writeForm.setPetAddress(petSitterDTO.getPetAddress());
        //writeForm.setBoardFile(petSitterDTO.getFileDTOList());
        logger.info("보드파일 뽑아봄= {}", writeForm.getBoardFile());


        logger.info("updatePetsitterForm:updatePetsitterDTO확인: {}", writeForm);


        return "mainboard/writeForm";
    }*/



    /*//게시글 수정
    @PostMapping (value = "/modify/{sitterNo}")
    public String mainBoardModify(@PathVariable Long sitterNo, Principal principal, WriteForm writeForm,
                                   PetSitterDTO petSitterDTO) {

        logger.info("MainBoardController-mainBoardModify()진입: {}");
        logger.info("수정 시 글번호 받아오는지 확인 sitterNo = {}", sitterNo);

        //수정할 글 번호 들고오기
        petSitterDTO = mainBoardService.getDetail(sitterNo);


        logger.info("글번호랑 받아온 게시글 내용들 확인 = {}, {}, {}", petSitterDTO.getPetContent(), petSitterDTO.getPetTitle(),petSitterDTO.getFileDTOList().isEmpty());

        //글작성자와 로그인한 유저가 동일하지 않으면 Exception

        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }


        //또 다른 PetSitterDTO를 생성해서 수정 정보들을 서비스에 넘겨주려함.

        mainBoardService.modify(writeForm, petSitterDTO);

        logger.info("새 객체 생성 후 수정된 정보가 담겼는지 확인 = {}, {}, {}", petSitterDTO.getPetContent(), petSitterDTO.getPetTitle(),petSitterDTO.getFileDTOList().isEmpty());

        return String.format("redirect:/mainboard/detail/%s", + sitterNo);
    }*/

}
