package com.pet.sitter.mainboard.controller;

import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.service.MainBoardService;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.security.Principal;
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
    public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page, Principal principal) {
        Page<PetSitterDTO> petSitterPage = null;
        if (principal!=null) {
            petSitterPage = mainBoardService.getListByMember(principal.getName(), page);
        } else {
            petSitterPage = mainBoardService.getList(page);
        }
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
    @GetMapping("/search")
    @ResponseBody
    //컨트롤러 메서드가 HTTP 응답을 만들 때, 해당 메서드의 리턴값을 HTTP 응답 본문에 직접 써서 데이터를 클라이언트에게 전송하는 역할
    public Page<PetSitterDTO> searchList(@RequestParam Map<String, Object> map, @RequestParam(value = "pno", defaultValue = "0") int pno) {
        String category = (String) map.get("category");
        String petCategory = (String) map.get("petCategory");
        String petAddress = (String) map.get("address");
        String day = (String) map.get("day");
        String timeStr = (String) map.get("time");
        Page<PetSitterDTO> petSitterDTOPage = mainBoardService.searchList(pno, category, petCategory, petAddress, day, timeStr);
        return petSitterDTOPage;
    }


    //recommend
    @PostMapping("/recommend")
    @ResponseBody
    public Page<PetSitterDTO> recommendList(@RequestParam Map<String, Object> map){
        String category = (String) map.get("category");
        String petCategory = (String) map.get("petCategory");
        String address = (String) map.get("sitterAddress");
        String sitterAddress = address.substring(0,2);
        Page<PetSitterDTO> petSitterDTOList = mainBoardService.recommendList(category, petCategory, sitterAddress);
        return petSitterDTOList;
    }


    //********************************혜지시작
    //글등록 폼 요청
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String writeForm(WriteForm writeForm) {
        logger.info("MainBoardController-writeForm()진입");
        return "mainboard/writeForm";
    }

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

    //추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/incrementLikes/{sitterNo}")
    public String incrementLikes (@PathVariable Long sitterNo,
                                  Principal principal) {
        mainBoardService.incrementLikes(sitterNo);
        return String.format("redirect:/mainboard/detail/{sitterNo}", sitterNo);
    }


    //제목으로 검색
    @GetMapping("/titleSearch")
    public String titleSearch () {
        return "mainboard/titleSearch";
    }

    //제목으로 검색
    @RequestMapping(value = "/titleSearch", method = {RequestMethod.GET, RequestMethod.POST})
    public String titleSearch (@RequestParam String keyword,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               Model model) {
        Page<PetSitterDTO> petSitterDTOPage = mainBoardService.titleSearch(keyword, page);
        //List<PetSitterDTO> petSitterDTOList = petSitterDTOPage.getContent();

        model.addAttribute("petSitterPage",petSitterDTOPage);
        model.addAttribute(keyword);
        //model.addAttribute("",petSitterDTOList);

        return "mainboard/list";
    }
}
