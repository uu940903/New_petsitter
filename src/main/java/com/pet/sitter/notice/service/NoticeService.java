package com.pet.sitter.notice.service;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.repository.NoticeFileRepository;
import com.pet.sitter.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class NoticeService {
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
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("noDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return noticeRepository.findAll(pageable);
    }

    @Transactional
    //공지게시판 글등록
    public void write(NoticeDTO noticeDTO, MultipartFile[] file) throws IOException {
        // 공지사항 엔티티 생성
        Notice notice = Notice.builder()
                .noTitle(noticeDTO.getNoTitle())
                .noContent(noticeDTO.getNoContent())
                .noDate(noticeDTO.getNoDate())
                .noViewCnt(0) // 조회수 초기값 설정
                .build();
        if (file[0].isEmpty()) {
            noticeRepository.save(notice);
        } else {
            Long boardNo = noticeRepository.save(notice).getNoNo();
            Notice notice1 = noticeRepository.findById(boardNo).get();

            NoticeFile noticeFile = new NoticeFile();

            String path = "C:/uploadfile/notice_img/";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile noFile : file) {
                String originalFilename = noFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                String noSavedPath = path + newFileName;
                File saveFile = new File(path, newFileName);
                noFile.transferTo(saveFile);

                noticeFile = NoticeFile.builder()
                        .noOrgNm(originalFilename)
                        .noSavedNm(newFileName)
                        .noSavedPath(noSavedPath)
                        .createDate(LocalDate.now())
                        .build();

                noticeFile.setNotice(notice1);
                noticeFileRepository.save(noticeFile);
            }

            noticeRepository.save(notice);
        }
    }
    //공지게시판 상세내용
    @Transactional
    public NoticeDTO getDetail(Long noNo) {
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.increaseViewCount();
            noticeRepository.save(notice);

            List<NoticeFile> fileList = new ArrayList<>(); // NoticeFile을 담을 리스트 선언
            for (NoticeFile noticeFile : notice.getNoticeFiles()) {
                fileList.add(noticeFile); // NoticeFile을 리스트에 추가
            }
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .noNo(notice.getNoNo())
                    .noTitle(notice.getNoTitle())
                    .noContent(notice.getNoContent())
                    .noDate(notice.getNoDate())
                    .noViewCnt(notice.getNoViewCnt())
                    .noticeFiles(fileList) // 첨부 파일 목록을 추가합니다.
                    .build();

            return noticeDTO;
        }
        return null;
    }

    //공지게시판 수정
    @Transactional
    public void getUpdate(Long noNo, NoticeDTO noticeDTO,MultipartFile[] newImageFiles) throws IOException {
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);

        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.setNoNo(noticeDTO.getNoNo());
            notice.setNoTitle(noticeDTO.getNoTitle());
            notice.setNoContent(noticeDTO.getNoContent());
            noticeRepository.save(notice);


            // 기존 파일 삭제
            List<NoticeFile> filesToDelete = notice.getNoticeFiles();
            for (NoticeFile delete : filesToDelete) {
                String filePath = delete.getNoSavedPath();
                File fileToDelete = new File(filePath);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
            }
            // 기존 파일 정보 삭제
            notice.getNoticeFiles().clear();

            // 새로운 파일 업로드 및 정보 저장
            if (newImageFiles[0].isEmpty()) {
                noticeRepository.save(notice);
            } else {
                Long boardNo = noticeRepository.save(notice).getNoNo();
                Notice notice1 = noticeRepository.findById(boardNo).get();

                NoticeFile noticeFile = new NoticeFile();

                String path = "C:/uploadfile/notice_img/";

                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                for (MultipartFile noFile : newImageFiles) {
                    String originalFilename = noFile.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String newFileName = UUID.randomUUID().toString() + fileExtension;
                    String noSavedPath = path + newFileName;
                    File saveFile = new File(path, newFileName);
                    noFile.transferTo(saveFile);

                    noticeFile = NoticeFile.builder()
                            .noOrgNm(originalFilename)
                            .noSavedNm(newFileName)
                            .noSavedPath(noSavedPath)
                            .createDate(LocalDate.now())
                            .build();

                    noticeFile.setNotice(notice1);
                    noticeFileRepository.save(noticeFile);
                }

                noticeRepository.save(notice);
            }
        }
    }

        //공지게시판 삭제
        @Transactional
        public void getDelete (Long noNo){
            Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
            if (noticeOptional.isPresent()) {
                Notice notice = noticeOptional.get();
                noticeRepository.delete(notice);
            }
        }

    }


