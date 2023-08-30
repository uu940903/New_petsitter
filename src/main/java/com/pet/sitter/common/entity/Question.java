package com.pet.sitter.common.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaNo;

    @Column
    @NotNull
    private String qnaTitle;

    @Column
    @NotNull
    private String qnaContent;

    @Column
    @NotNull
    private LocalDateTime qnaDate;

    @Column
    @NotNull
    private Integer qnaPw;

    @Column
    @NotNull
    private Integer qnaViewCnt;

    @Column
    private String qnaFile;

    @Column
    private String qnaComment;

    @ManyToOne
    @JoinColumn (name = "id", referencedColumnName = "id")
    private Member member;

    @Builder

    public Question(Long qnaNo, String qnaTitle, String qnaContent, LocalDateTime qnaDate, Integer qnaPw, Integer qnaViewCnt, String qnaFile, String qnaComment, Member member) {
        this.qnaNo = qnaNo;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.qnaDate = qnaDate;
        this.qnaPw = qnaPw;
        this.qnaViewCnt = qnaViewCnt;
        this.qnaFile = qnaFile;
        this.qnaComment = qnaComment;
        this.member = member;
    }
}
