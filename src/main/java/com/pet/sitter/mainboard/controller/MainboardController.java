package com.pet.sitter.mainboard.controller;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.mainboard.service.MainBoardService;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/mainboard")
@Controller
public class MainBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainBoardService mainBoardService;

    @Autowired
    public MemberService memberService;


    @GetMapping("/list")
    public String getList(Model model) {
        List<PetSitterDTO> petSitterList = mainBoardService.getList();
        model.addAttribute("petSitterList", petSitterList);
        System.out.println("list 크기"+petSitterList.size());
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





    //********************************혜지시작
    //AreaSearch 테이블에 insert


    //글등록 폼 요청
    //!!!!!스프링시큐리티 적용되면 @PreAuthorize("isAuthenticated()") //로그인했니? 추가해야됨
    //!!!!@Vaild 추가해야됨
    @GetMapping("/write")
    public String writeForm(WriteForm writeForm) {
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

        for (int i =0; i< boardFile.length; i++){
            logger.info("파일: {}", boardFile[i].getOriginalFilename());
        }

        mainBoardService.write(petSitterDTO, id, boardFile);


        return String.format("redirect:/mainboard/list");
    }

    //게시글 수정 폼 요청
    @GetMapping("/modify/{sitterNo}")
    public String updatePetsitterForm (@PathVariable Long sitterNo, Principal principal,
                                       WriteForm writeForm, PetSitterFileDTO petSitterFileDTO) {

        //글 번호 들고오기
        PetSitterDTO petSitterDTO = mainBoardService.updateForm(sitterNo);

        //글작성자와 로그인한 유저가 동일하지 않으면 Exception
        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        petSitterDTO.getFileDTOList();
        //petSitterDTO.getFileDTOList().get().setSitterNo();

      /*  for (PetSitterFileDTO sitterNoForFile : petSitterDTO.getFileDTOList()) {
            logger.info("컨트롤러에서 시터엔오포파일 확인: {}", sitterNoForFile.getSitterNo());
        }*/

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
    }



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

/*    //게시글 삭제
    @GetMapping ("/delete/{sitterNo}")
    public String deletePetSitter (@PathVariable Long sitterNo, Principal principal) {

        //글번호 들고오기
        PetSitterDTO petSitterDTO = mainBoardService.getDetail(sitterNo);

        //글작성자와 로그인한 유저가 동일하지 않으면 Exception
        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        mainBoardService.delete(petSitterDTO);

        return "redirect:/mainboard/list";

    }*/

    //게시글 삭제
    @GetMapping ("/delete/{sitterNo}")
    public String deletePetSitter (@PathVariable Long sitterNo, Principal principal) {

        //글번호 들고오기
        PetSitterDTO petSitterDTO = mainBoardService.getDetail(sitterNo);

        Petsitter petsitter = petSitterDTO.dtoToEntity(petSitterDTO);

        //글작성자와 로그인한 유저가 동일하지 않으면 Exception
        if (!petSitterDTO.getMember().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        mainBoardService.deletePetSitter(petsitter);

        return "redirect:/mainboard/list";

    }

}
