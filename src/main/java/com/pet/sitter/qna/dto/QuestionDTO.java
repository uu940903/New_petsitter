package com.pet.sitter.qna.dto;


import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.common.entity.QuestionFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

    private Member member;
    private List<MultipartFile> file;
    private List<QuestionFile> questionList;
    private List<Answer> answerList;

    //새로운 업로드 파일을 저장
    private List<MultipartFile> newImageFiles;

    public Question toEntity(){
        Question build = Question.builder()
                .qnaNo(qnaNo)
                .qnaTitle(qnaTitle)
                .qnaContent(qnaContent)
                .qnaDate(qnaDate)
                .qnaPw(qnaPw)
                .qnaViewCnt(qnaViewCnt)
                .qnaFile(qnaFile)
                .member(member)
                .build();
        return build;
    }

    @Builder
    public QuestionDTO(Long qnaNo, String qnaTitle, String qnaContent, LocalDateTime qnaDate, String qnaPw, Integer qnaViewCnt, String qnaFile, Member member,List<MultipartFile> file,List<QuestionFile> questionList,List<MultipartFile> newImageFiles,List<Answer> answerList) {
        this.qnaNo = qnaNo;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.qnaDate = qnaDate;
        this.qnaPw = qnaPw;
        this.qnaViewCnt = qnaViewCnt;
        this.qnaFile = qnaFile;
        this.member = member;
        this.file = file;
        this.questionList = questionList;
        this.newImageFiles = newImageFiles;
        this.answerList = answerList;
    }
}
