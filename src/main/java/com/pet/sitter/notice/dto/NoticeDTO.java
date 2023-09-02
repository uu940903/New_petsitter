package com.pet.sitter.notice.dto;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDTO {
    private Long noNo;
    private String noTitle;
    private String noContent;
    private LocalDateTime noDate =LocalDateTime.now();
    private Integer noViewCnt;

    // 파일 업로드를 위한 MultipartFile 필드 추가
    private MultipartFile noticeFile;


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

    public NoticeFile toFileEntity() {
        // 파일 업로드 관련 정보를 NoticeFile 엔티티로 매핑
        String originalFilename = noticeFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 파일 확장자 추출
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        return NoticeFile.builder()
                .noOrgNm(originalFilename)
                .noSavedNm(uniqueFilename)
                .noSavedPath("C:\\uploadfile\\")
                .build();
    }

    @Builder
    public NoticeDTO(Long noNo, String noTitle, String noContent, LocalDateTime noDate, Integer noViewCnt ) {
        this.noNo = noNo;
        this.noTitle = noTitle;
        this.noContent = noContent;
        this.noDate = noDate;
        this.noViewCnt = noViewCnt;

    }

}


