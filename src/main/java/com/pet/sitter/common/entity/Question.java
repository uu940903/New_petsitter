package com.pet.sitter.common.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qna_no;

    @Column
    @NotNull
    private String qna_title;

    @Column
    @NotNull
    private String qna_content;

    @Column
    @NotNull
    private LocalDateTime qna_date;

    @Column
    @NotNull
    private Integer qna_pw;

    @Column
    @NotNull
    private Integer qna_view_cnt;

    @Column
    private String qna_file;

    @Column
    private String qna_comment;

    @ManyToOne
    @JoinColumn (name = "id", referencedColumnName = "id")
    private Member member;

}
