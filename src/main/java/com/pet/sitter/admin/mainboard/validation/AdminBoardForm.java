package com.pet.sitter.admin.mainboard.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

//질문등록폼()의 입력값에 대한 유효성검사
@Getter
@Setter
public class AdminBoardForm {
    @NotEmpty(message = "제목은 필수입력입니다")
    private String petTitle;//제목

    //내용
    @NotEmpty(message = "내용은 필수입력입니다")
    private String petContent;//내용
}
