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
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long noFile;

    @Column
    private String noOrgNm;

    @Column
    private String noSavedNm;

    @Column
    private String noSavedPath;

    // NoticeFile 엔티티에서 Notice와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "noNo", referencedColumnName = "noNo")
    private Notice notice;

    @Column
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.createDate = LocalDate.now();
    }

    @Builder
    public NoticeFile(Long noFile, String noOrgNm, String noSavedNm, String noSavedPath, Notice notice, LocalDate createDate) {
        this.noFile = noFile;
        this.noOrgNm = noOrgNm;
        this.noSavedNm = noSavedNm;
        this.noSavedPath = noSavedPath;
        this.notice = notice;
        this.createDate = createDate;
    }
}