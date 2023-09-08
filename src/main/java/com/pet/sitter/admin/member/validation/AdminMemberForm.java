package com.pet.sitter.admin.member.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

//질문등록폼()의 입력값에 대한 유효성검사
@Getter
@Setter
public class AdminMemberForm {
    @NotEmpty(message = "아이디은 필수입력입니다")
    private String memberId;

    //내용
    @NotEmpty(message = "이름은 필수입력입니다")
    private String name;

    @NotEmpty(message = "핸드폰번호은 필수입력입니다")
    private String phone;

    @NotEmpty(message = "이메일은 필수입력입니다")
    private String eMail;

    @NotEmpty(message = "생년월일은 필수입력입니다")
    private String birth;

    @NotEmpty(message = "주소은 필수입력입니다")
    private String address;

    @NotEmpty(message = "닉네임은 필수입력입니다")
    private String nickname;

}
