package com.pet.sitter.notice.controller;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;



    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //공지게시판 목록보기
    @GetMapping("/list")
    public String list(Model model,@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Notice> noticePage = noticeService.getNoticeList(page);
        model.addAttribute("noticePage", noticePage);
        return "notice/NoticeList";
    }


    //공지게시판 글작성폼보여주기
    @GetMapping("/write")
    public String write() {
        return "notice/NoticeForm";
    }



    //공지게시판 글 작성하기
    @PostMapping("/write")
        public String write(@ModelAttribute NoticeDTO noticeDTO,@RequestParam("file") MultipartFile[] file, Model model) throws IOException {
        try {
            noticeService.write(noticeDTO, file);
            return "redirect:/notice/list";
        } catch (IOException e) {
            // 파일 업로드 중 오류가 발생한 경우에 대한 예외 처리
            e.printStackTrace();
            model.addAttribute("errorMessage", "글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }

    //공지게시판 상세내용 조회

    @GetMapping("/detail/{noNo}")
    public String detail(@PathVariable("noNo") Long noNo, Model model) {
        NoticeDTO noticeDTO = noticeService.getDetail(noNo);
        model.addAttribute("noticeDTO", noticeDTO);

        return "notice/NoticeDetail";
    }
    //공지게시판 수정폼
    @GetMapping("/edit/{noNo}")
    public String edit(@PathVariable("noNo") Long noNo, Model model){
        NoticeDTO noticeDTO = noticeService.getDetail(noNo);

        model.addAttribute("noticeDTO",noticeDTO);
        System.out.println("여기!!!!!!!!!!!1"+noticeDTO);
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
    @DeleteMapping("/delete/{noNo}")
    public String getDelete(NoticeDTO noticeDTO, Model model){
        noticeService.getDelete(noticeDTO);
        model.addAttribute("noticeDTO",noticeDTO);
        return "redirect:/notice/list";
    }

}
