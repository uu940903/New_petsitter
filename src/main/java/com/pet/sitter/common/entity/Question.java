package com.pet.sitter.common.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "qna_date", columnDefinition = "TIMESTAMP")
    @NotNull
    private LocalDateTime qnaDate;

    @Column
    private String qnaPw;

    @Column
    @NotNull
    private Integer qnaViewCnt;

    @Column
    private String qnaFile;


    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionFile> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    public List<QuestionFile> getQuestionList() {
        return questionList;
    }

    public void setQuestionFile(List<QuestionFile> questionList) {
        this.questionList = questionList;
    }

    //조회수증가
    public void increaseViewCount() {
        this.qnaViewCnt++;
    }

    @Builder
    public Question(Long qnaNo, String qnaTitle, String qnaContent, LocalDateTime qnaDate, String qnaPw, Integer qnaViewCnt, String qnaFile, Member member, List<QuestionFile> questionList, List<Answer> answerList) {
        this.qnaNo = qnaNo;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.qnaDate = qnaDate;
        this.qnaPw = qnaPw;
        this.qnaViewCnt = qnaViewCnt;
        this.qnaFile = qnaFile;
        this.member = member;
        this.questionList = questionList;
        this.answerList = answerList;
    }
}
