package com.pet.sitter.qna.dto;

import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Data
public class AnswerDTO {

    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Question question;
    private Member member;


    public Answer toEntity() {
        Answer answer = Answer.builder()
                .id(id)
                .content(content)
                .question(question)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .member(member)
                .build();
        return answer;
    }

    @Builder
    public AnswerDTO(Long id,String content,LocalDateTime createdDate, LocalDateTime modifiedDate,Question question,Member member){
        this.id=id;
        this.content=content;
        this.question=question;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
        this.member=member;
    }
}
