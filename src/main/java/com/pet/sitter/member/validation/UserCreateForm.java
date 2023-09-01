package com.pet.sitter.member.validation;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//SiteUser Entity관련 유효성검사용 클래스
//회원가입폼 페이지에서 입력.선택사항에 적용되는 유효성검사 클래스
public class UserCreateForm {
    @Size(min = 3, max = 20)
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
    private String eMail;

    @NotEmpty(message = "생년월일은 필수입력입니다.")
    private String birth;

    @NotEmpty(message = "주소는 필수입력입니다.")
    private String address;

    @NotEmpty(message = "상세주소는 필수입력입니다.")
    private String detailaddress;

    @NotEmpty(message = "우편번호는 필수입력입니다.")
    private String zipcode;

    @NotEmpty(message = "별칭은 필수입력입니다.")
    private String nickname;

}