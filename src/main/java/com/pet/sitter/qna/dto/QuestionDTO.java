package com.pet.sitter.qna.dto;


import com.pet.sitter.common.entity.Question;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionDTO {

    private Long qnaNo;
    private String qnaTitle;
    private String qnaContent;
    private LocalDateTime qnaDate =LocalDateTime.now();
    private String qnaPw;
    private Integer qnaViewCnt = 0;
    private String qnaFile;
    private String qnaComment;


    public Question toEntity(){
        Question build = Question.builder()
                .qnaNo(qnaNo)
                .qnaTitle(qnaTitle)
                .qnaContent(qnaContent)
                .qnaDate(qnaDate)
                .qnaPw(qnaPw)
                .qnaViewCnt(qnaViewCnt)
                .qnaFile(qnaFile)
                .qnaComment(qnaComment)
                .build();
        return build;
    }

    @Builder
    public QuestionDTO(Long qnaNo, String qnaTitle, String qnaContent, LocalDateTime qnaDate, String qnaPw, Integer qnaViewCnt, String qnaFile, String qnaComment) {
        this.qnaNo = qnaNo;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.qnaDate = qnaDate;
        this.qnaPw = qnaPw;
        this.qnaViewCnt = qnaViewCnt;
        this.qnaFile = qnaFile;
        this.qnaComment = qnaComment;
    }
}
