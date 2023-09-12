package com.pet.sitter.qna.dto;

import com.pet.sitter.common.entity.QuestionFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class QuestionFileDTO {

    private Long Qfile;
    private Long QnaNo;
    private String QOrgNm;
    private String QSavedNm;
    private String QSavedPath;
    private LocalDate QcreateDate;

    public QuestionFile toFileEntity() {
        return QuestionFile.builder()
                .Qfile(Qfile)
                .QOrgNm(QOrgNm)
                .QSavedNm(QSavedNm)
                .QSavedPath(QSavedPath)
                .QcreateDate(QcreateDate)
                .build();
    }

    @Builder
    public QuestionFileDTO(Long Qfile, Long QnaNo, String QOrgNm, String QSavedNm,
                           String QSavedPath, LocalDate QcreateDate) {
        this.Qfile = Qfile;
        this.QnaNo = QnaNo;
        this.QOrgNm = QOrgNm;
        this.QSavedNm = QSavedNm;
        this.QSavedPath = QSavedPath;
        this.QcreateDate = QcreateDate;
    }
}