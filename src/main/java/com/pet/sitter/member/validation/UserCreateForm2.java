package com.pet.sitter.member.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserCreateForm2 {

    @NotEmpty(message = "비밀번호는 필수입력입니다.")
    private String newPassword1;   //비밀번호

    @NotEmpty(message = "비밀번호 확인은 필수입력입니다.")
    private String newPassword2;   //(form.html문서에 존재하는 )확인용 비밀번호
}
