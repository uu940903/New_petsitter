package com.pet.sitter.qna.dto;

import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
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
}
