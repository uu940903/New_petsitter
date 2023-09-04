package com.pet.sitter.notice.service;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.repository.NoticeFileRepository;
import com.pet.sitter.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class NoticeService {
    @Value("${resource.location}")
    private String fileDir;
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;


    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
    }

    @Transactional
    public Long savePost(NoticeDTO noticeDTO) {
        return noticeRepository.save(noticeDTO.toEntity()).getNoNo();
    }

    //공지게시판목록조회
    @Transactional
    public List<NoticeDTO> getNoticeList() {
        List<Notice> notices = noticeRepository.findAllByOrderByNoNoDesc();
        List<NoticeDTO> noticeDTOList = new ArrayList<>();

        for (Notice notice : notices) {
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .noNo(notice.getNoNo())
                    .noTitle(notice.getNoTitle())
                    .noContent(notice.getNoContent())
                    .noDate(notice.getNoDate())
                    .noViewCnt(notice.getNoViewCnt())
                    .build();
            noticeDTOList.add(noticeDTO);
        }
        return noticeDTOList;
    }

    @Transactional
    //공지게시판 글등록
    public void write(NoticeDTO noticeDTO, NoticeFile noticeFile) throws IOException {

        noticeRepository.save(noticeDTO.toEntity());
        noticeFileRepository.save(noticeFile);

    }





    //공지게시판 상세내용
    @Transactional
    public NoticeDTO getDetail(Long noNo){
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
        Notice notice = noticeOptional.get();


        NoticeDTO noticeDTO = NoticeDTO.builder()
                .noNo(notice.getNoNo())
                .noTitle(notice.getNoTitle())
                .noContent(notice.getNoContent())
                .noDate(notice.getNoDate())
                .noViewCnt(notice.getNoViewCnt())
                .build();
        return noticeDTO;
    }

    //공지게시판 수정
    @Transactional
    public void getUpdate(Long noNo, NoticeDTO noticeDTO) throws IOException {
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.setNoNo(noticeDTO.getNoNo());
            notice.setNoTitle(noticeDTO.getNoTitle());
            notice.setNoContent(noticeDTO.getNoContent());

            /*Long fileno = notice.getNo_no();
            MultipartFile noticeFile = noticeDTO.getNoticeFile();
            String originfilename =noticeFile.getOriginalFilename();
            String sotredFileName = "Notcie" + fileno + "_"+ originfilename; //게시글no으로변경예정.
            String savePath = "D:\\project\\New_petsitter\\src\\main\\resources\\static\\notice\\img\\uploadfile\\" + sotredFileName;
            noticeFile.transferTo(new File(savePath));

            notice.setNo_file(originfilename);
            notice.setNo_filename(sotredFileName);
            notice.setNo_filepath(savePath);*/
            noticeRepository.save(notice);
        }
    }

    //공지게시판 삭제
    @Transactional
    public void getDelete(NoticeDTO noticeDTO) {
        System.out.println("Adfasdf");
        Optional<Notice> noticeOptional = noticeRepository.findById(noticeDTO.getNoNo());
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.setNoNo(noticeDTO.getNoNo());
            notice.setNoTitle(noticeDTO.getNoTitle());
            notice.setNoContent(noticeDTO.getNoContent());
            noticeRepository.delete(notice);
        }
    }

    public Long getNo(){
       Notice notice = noticeRepository.findTopByOrderByNoNoDesc();
       Long nono = notice.getNoNo();
       return nono;
    }

}


