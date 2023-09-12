package com.pet.sitter.mypage.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyForm {

    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "폰번호 입력하세요")
    private String phone;

    @NotEmpty(message = "주소 입력하세요")
    private String address;

    @NotEmpty(message = "닉네임 입력하세요")
    private String nickname;
}
