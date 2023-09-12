package com.pet.sitter.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table
@Entity
public class QuestionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Qfile;

    @Column
    private String QOrgNm;

    @Column
    private String QSavedNm;

    @Column
    private String QSavedPath;

    // NoticeFile 엔티티에서 Notice와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "qnaNo", referencedColumnName = "qnaNo")
    private Question question;

    @Column
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate QcreateDate; // 날짜

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.QcreateDate = LocalDate.now();
    }

    @Builder
    public QuestionFile(Long Qfile, String QOrgNm, String QSavedNm, String QSavedPath, Question question, LocalDate QcreateDate) {
        this.Qfile = Qfile;
        this.QOrgNm = QOrgNm;
        this.QSavedNm = QSavedNm;
        this.QSavedPath = QSavedPath;
        this.question = question;
        this.QcreateDate = QcreateDate;
    }
}
