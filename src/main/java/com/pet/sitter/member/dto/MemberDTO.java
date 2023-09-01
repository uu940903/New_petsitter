package com.pet.sitter.member.dto;

import com.pet.sitter.common.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String memberId;
    private String pw;
    private String name;
    private String phone;
    private String eMail;
    private String birth;
    private String address;
    private String detailaddress;
    private String zipcode;
    private String nickname;
    private String isshow;

    @Builder
    public MemberDTO(Member member) {
        this.id = member.getId();
        this.memberId = member.getMemberId();
        this.pw = member.getPw();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.eMail = member.getEMail();
        this.birth = member.getBirth();
        this.address = member.getAddress();
        this.detailaddress = member.getDetailaddress();
        this.zipcode = member.getZipcode();
        this.nickname = member.getNickname();
        this.isshow = member.getIsshow();
    }


    public Member toEntity(){
        return Member.builder()
                .id(this.id)
                .memberId(this.memberId)
                .pw(this.pw)
                .name(this.name)
                .phone(this.phone)
                .eMail(this.eMail)
                .birth(this.birth)
                .address(this.address)
                .nickname(this.nickname)
                .isshow(this.isshow)
                .build();
    }
}
