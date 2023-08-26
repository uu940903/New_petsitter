package com.pet.sitter.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    private String id;
    private String pw;
    private String name;
    private String phone;
    private String e_mail;
    private String birth;
    private String address;
    private String nickname;
    private char isshow;

    public MemberDTO() {
    }

    public MemberDTO(String id, String pw, String name, String phone, String e_mail, String birth, String address, String nickname, char isshow) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.e_mail = e_mail;
        this.birth = birth;
        this.address = address;
        this.nickname = nickname;
        this.isshow = isshow;
    }

    public MemberDTO(String id, String name, String nickname) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;

    }
}
