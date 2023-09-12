package com.pet.sitter.notice.dto;

import com.pet.sitter.common.entity.NoticeFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

    public class NoticeFileDTO {
        private Long noFile;
        private Long noNo;
        private String noOrgNm;
        private String noSavedNm;
        private String noSavedPath;
        private LocalDate createDate;

        public NoticeFile toFileEntity() {
            return NoticeFile.builder()
                    .noFile(noFile)
                    .noOrgNm(noOrgNm)
                    .noSavedNm(noSavedNm)
                    .noSavedPath(noSavedPath)
                    .createDate(createDate)
                    .build();
        }

        @Builder
        public NoticeFileDTO(Long noFile, Long noNo, String noOrgNm, String noSavedNm, String noSavedPath, LocalDate createDate) {
            this.noFile = noFile;
            this.noNo = noNo;
            this.noOrgNm = noOrgNm;
            this.noSavedNm = noSavedNm;
            this.noSavedPath = noSavedPath;
            this.createDate = createDate;
        }
    }