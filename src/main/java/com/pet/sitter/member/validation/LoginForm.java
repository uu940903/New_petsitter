package com.pet.sitter.member.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "아이디, 비밀번호는 필수 입력입니다.")
    private String id;
    private String pw;
}
