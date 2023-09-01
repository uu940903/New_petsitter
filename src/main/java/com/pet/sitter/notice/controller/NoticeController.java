package com.pet.sitter.notice.controller;

import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Value("${resource.location}")
    private String fileDir;
    private final NoticeService noticeService;



    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //공지게시판 목록보기
    @GetMapping("/list")
    public String list(Model model) {
        List<NoticeDTO> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        System.out.println("폴더 번호 = "+getFolder());
        return "notice/NoticeList";
    }

    //공지게시판 글작성폼보여주기
    @GetMapping("/write")
    public String write() {
        return "notice/NoticeForm";
    }


    //저장 폴더 생성
    private String getFolder() {
        Long nono = noticeService.getNo();
        nono+=1;
        String StrNo = nono.toString();
        System.out.println("StrNo = "+StrNo);
        return StrNo;
    }
    
    
    //공지게시판 글 작성하기
    @PostMapping("/write")
    public String write(MultipartFile files, NoticeDTO noticeDTO) throws IOException {
        NoticeFile noticeFile = new NoticeFile();
        noticeDTO.setNoViewCnt(0);

        String strNo = getFolder();
        Long noNo = Long.parseLong(strNo);
        System.out.println("noNo = "+noNo);
        //noticeFile.getNotice().setNoNo(noNo);

        /*File uplodaPath = new File(fileDir, getFolder());

        if (!uplodaPath.exists()){
            uplodaPath.mkdirs();
        } */

        noticeFile.setNoOrgNm(files.getOriginalFilename());     //noticeFile의 noOrgNm 필드에 getOriginalFilename()의 값을 저장

        // 원래 파일 이름 추출
        String origName = noticeFile.getNoOrgNm();
        origName = origName.substring(origName.lastIndexOf("\\")+1);
        System.out.println("origName = "+origName);
        
        //UUID생성
        UUID uuid = UUID.randomUUID();
        String noSavedNm = uuid.toString()+"_"+origName;
        System.out.println("noSavedNm = "+noSavedNm);

        noticeFile.setNoSavedNm(noSavedNm);
        //noticeFile.setNoSavedPath(uplodaPath.getPath());

        File saveFile = new File(noSavedNm);
        //File saveFile = new File(uplodaPath, noSavedNm);

        try{
            files.transferTo(saveFile);
        }catch (IOException e){
           throw new RuntimeException(e);
        }



        noticeDTO.setNoticeFile(noticeFile);
        noticeDTO.getNoticeFile().toString();

        noticeService.write(noticeDTO, noticeFile);
        System.out.println("getNoSavedPath");


        return "redirect:/notice/list";
    }


    //공지게시판 상세내용 조회

    @GetMapping("/detail/{noNo}")
    public String detail(@PathVariable("noNo") Long noNo, Model model) {
        NoticeDTO noticeDTO = noticeService.getDetail(noNo);
        model.addAttribute("noticeDTO", noticeDTO);
        
        //경로+폴더
        String path = fileDir;
        //String savename  =noSavedNm;
        //String path = fileDir+noticeDTO.getNoNo();
        NoticeFile noticeFile = new NoticeFile();
        noticeFile.setNoSavedPath(path);
        noticeFile.setNoSavedPath(path);
        model.addAttribute("noticeFile", noticeFile);
        return "notice/NoticeDetail";
    }

    //공지게시판 수정폼
    @GetMapping("/edit/{noNo}")
    public String edit(@PathVariable("no_no") Long noNo, Model model){
        NoticeDTO noticeDTO = noticeService.getDetail(noNo);

        model.addAttribute("noticeDTO",noticeDTO);
        return "notice/NoticeUpdate";
    }

    //공지게시판 수정하기
    @PutMapping("/update")
    public String getUpdate(Model model,Long noNo,NoticeDTO noticeDTO) throws IOException {
        noticeService.getUpdate(noNo,noticeDTO);
        model.addAttribute("noticeDTO",noticeDTO);
        return "redirect:/notice/list";
    }


    //공지게시판 삭제처리
    @DeleteMapping("/delete/{no_no}")
    public String getDelete(NoticeDTO noticeDTO, Model model){
        noticeService.getDelete(noticeDTO);
        model.addAttribute("noticeDTO",noticeDTO);
        return "redirect:/notice/list";
    }

}
