package com.pet.sitter.mypage.validation;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PassModifyForm {

    @NotEmpty(message = "비밀번호는 필수입력입니다.")
    private String password1;//비밀번호

    @NotEmpty(message = "비밀번호 학인은 필수입력입니다.")
    private String password2;//(form.html문서에 존재하는 )확인용 비밀번호


}
