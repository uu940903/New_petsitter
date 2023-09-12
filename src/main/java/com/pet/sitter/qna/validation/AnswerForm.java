package com.pet.sitter.qna.validation;

import com.pet.sitter.common.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long id;

    private Member member;
}
