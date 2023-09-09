package com.pet.sitter.member.validation;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//SiteUser Entity관련 유효성검사용 클래스
//회원가입폼 페이지에서 입력.선택사항에 적용되는 유효성검사 클래스
public class UserCreateForm {
    @Size(min = 3, max = 255)
    @NotEmpty(message = "ID는 필수입력입니다.")
    private String memberId;//회원명

    @NotEmpty(message = "비밀번호는 필수입력입니다.")
    private String pw1;//비밀번호

    @NotEmpty(message = "비밀번호를 확인해주세요.")
    private String pw2;//비밀번호


    @NotEmpty(message = "이름을 작성해주세요.")
    private String name;

    @NotEmpty(message = "휴대번호는 필수입력입니다.")
    private String phone;

    @NotEmpty(message = "이메일은 필수입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String eMail;

    @NotEmpty(message = "생년월일은 필수입력입니다.")
//    @Pattern(regexp = "\\d{8}", message = "생년월일은 8자리 숫자로 입력해주세요 (예: 20210905)")
    private String birth;

    @NotEmpty(message = "주소는 필수입력입니다.")
    private String address;

    @NotEmpty(message = "상세주소는 필수입력입니다.")
    private String detailaddress;

    @NotEmpty(message = "우편번호는 필수입력입니다.")
    private String zipcode;

    @NotEmpty(message = "별칭은 필수입력입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;


}