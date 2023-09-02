package com.pet.sitter.notice.service;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.repository.NoticeFileRepository;
import com.pet.sitter.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private String uploadDirectory;
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
    public Page<Notice> getNoticeList(int page) {
        List <Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("noDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return noticeRepository.findAll(pageable);

    }

    @Transactional
    //공지게시판 글등록
    public void write(NoticeDTO noticeDTO, NoticeFile noticeFile) throws IOException {
// 1. 업로드된 파일을 저장할 디렉토리 생성
        String folderPath = fileDir + getFolder(); // 파일을 저장할 디렉토리 경로
        File uploadPath = new File(folderPath);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 2. 첨부 파일 관련 정보 설정
        MultipartFile multipartFile = noticeDTO.getNoticeFile().getNoFile();

        // 원래 파일 이름 추출
        String originalFilename = multipartFile.getOriginalFilename();
        String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 첨부 파일을 저장할 경로 설정
        String savedFilePath = folderPath + File.separator + savedFilename;

        // 3. 공지 게시물 엔터티 생성 및 첨부 파일과의 관계 설정
        Notice notice = noticeDTO.toEntity();
        noticeFile.setNoOrgNm(originalFilename);
        noticeFile.setNoSavedNm(savedFilename);
        noticeFile.setNoSavedPath(savedFilePath);
        notice.setNoticeFile(noticeFile);

        // 4. 첨부 파일 저장
        multipartFile.transferTo(new File(savedFilePath));

        // 5. 공지 게시물과 첨부 파일 저장
        noticeRepository.save(notice);
        noticeFileRepository.save(noticeFile);
    }

//        noticeRepository.save(noticeDTO.toEntity());
//        noticeFileRepository.save(noticeFile);

    }


    //공지게시판 상세내용
    @Transactional
    public NoticeDTO getDetail(Long noNo) {
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.increaseViewCount();
            noticeRepository.save(notice);

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .noNo(notice.getNoNo())
                    .noTitle(notice.getNoTitle())
                    .noContent(notice.getNoContent())
                    .noDate(notice.getNoDate())
                    .noViewCnt(notice.getNoViewCnt())
                    .build();
            return noticeDTO;
        }
        return null;
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


