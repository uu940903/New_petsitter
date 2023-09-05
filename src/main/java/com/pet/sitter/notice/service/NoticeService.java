package com.pet.sitter.notice.service;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.notice.dto.NoticeFileDTO;
import com.pet.sitter.notice.repository.NoticeFileRepository;
import com.pet.sitter.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


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
     //          noFile.transferTo(new File(noSavedPath));

                noticeFile = NoticeFile.builder()
                        .noOrgNm(originalFilename)
                        .noSavedNm(newFileName)
                        .noSavedPath(noSavedPath)
//                        .createDate(noticeFileDTO.getCreateDate())
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
    public void getUpdate(Long noNo, NoticeDTO noticeDTO) throws IOException {
        MultipartFile[] file = new MultipartFile[0];
        Optional<Notice> noticeOptional = noticeRepository.findById(noNo);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            notice.setNoNo(noticeDTO.getNoNo());
            notice.setNoTitle(noticeDTO.getNoTitle());
            notice.setNoContent(noticeDTO.getNoContent());
            noticeRepository.save(notice);

            String path = "C:/uploadfile/notice_img/";

            for (MultipartFile nofile : file) {
                if (file == null) {
                    // 파일을 업로드하고 이전 파일을 대체하는 로직 추가
                    String originalFilename = nofile.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String newFileName = UUID.randomUUID().toString() + fileExtension;
                    String noSavedPath = path + newFileName;

                    try {
                        nofile.transferTo(new File(noSavedPath));

                        // 이전 첨부 파일 삭제 (선택사항)
                        // 여기서 이전 첨부 파일을 DB에서 삭제하고, 파일 시스템에서 삭제할 수 있습니다.
                        // 이전 파일을 삭제하지 않고 저장하는 것도 가능합니다.

                        // 이전 첨부 파일 엔티티 삭제 (선택사항)
                        // 이전 첨부 파일 엔티티를 DB에서 삭제합니다.
                        // noticeFileRepository.deleteById(notice.getNoticeFiles().get(0).getNoFile());

                        // 새로운 첨부 파일 엔티티 생성
                        NoticeFile newNoticeFile = NoticeFile.builder()
                                .noOrgNm(originalFilename)
                                .noSavedNm(newFileName)
                                .noSavedPath(noSavedPath)
                                .notice(notice)
                                .build();

                        // 새로운 첨부 파일 엔티티 저장
                        noticeFileRepository.save(newNoticeFile);

                        // 이전 첨부 파일 엔티티 대체 (선택사항)
                        // notice.getNoticeFiles().clear();
                        // notice.getNoticeFiles().add(newNoticeFile);

                        // 공지사항 엔티티에 첨부 파일 정보 설정
                        List<NoticeFile> noticeFiles = new ArrayList<>();
                        noticeFiles.add(newNoticeFile);
                        notice.setNoticeFiles(noticeFiles);

                    } catch (IOException e) {
                        throw new MultipartException("파일 업로드 중 오류가 발생했습니다.");
                    }
                }

                noticeRepository.save(notice);
            }
        }
    }
        //공지게시판 삭제
        @Transactional
        public void getDelete (NoticeDTO noticeDTO){
            Optional<Notice> noticeOptional = noticeRepository.findById(noticeDTO.getNoNo());
            if (noticeOptional.isPresent()) {
                Notice notice = noticeOptional.get();
                notice.setNoNo(noticeDTO.getNoNo());
                notice.setNoTitle(noticeDTO.getNoTitle());
                notice.setNoContent(noticeDTO.getNoContent());
                noticeRepository.delete(notice);
            }
        }

    }


