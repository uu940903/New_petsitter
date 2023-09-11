package com.pet.sitter.notice.dto;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDTO {
    private Long noNo;
    private String noTitle;
    private String noContent;
    private LocalDateTime noDate =LocalDateTime.now();
    private Integer noViewCnt = 0;

    // 파일 업로드를 위한 MultipartFile 필드 추가
    private List<MultipartFile> file;
    private List<NoticeFile> noticeFiles;

    //새로운 업로드 파일을 저장
    private List<MultipartFile> newImageFiles;




    public Notice toEntity() {
        Notice build = Notice.builder()
                .noNo(noNo)
                .noTitle(noTitle)
                .noContent(noContent)
                .noDate(noDate)
                .noViewCnt(noViewCnt)
                .build();
        return build;
    }



    @Builder
    public NoticeDTO(Long noNo, String noTitle, String noContent, LocalDateTime noDate, Integer noViewCnt,List<MultipartFile> file,List<NoticeFile> noticeFiles,List<MultipartFile> newImageFiles) {
        this.noNo = noNo;
        this.noTitle = noTitle;
        this.noContent = noContent;
        this.noDate = noDate;
        this.noViewCnt = noViewCnt;
        this.file =file;
        this.noticeFiles=noticeFiles;
        this.newImageFiles = newImageFiles;
    }

}


