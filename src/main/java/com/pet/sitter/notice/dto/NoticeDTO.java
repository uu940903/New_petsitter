package com.pet.sitter.notice.dto;

import com.pet.sitter.common.entity.Notice;
import com.pet.sitter.common.entity.NoticeFile;
import lombok.*;

import java.time.LocalDateTime;

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

    //Controller파일담는용도
    @Getter
    private NoticeFile noticeFile;


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
    public NoticeDTO(Long noNo, String noTitle, String noContent, LocalDateTime noDate, Integer noViewCnt ) {
        this.noNo = noNo;
        this.noTitle = noTitle;
        this.noContent = noContent;
        this.noDate = noDate;
        this.noViewCnt = noViewCnt;

    }

}


