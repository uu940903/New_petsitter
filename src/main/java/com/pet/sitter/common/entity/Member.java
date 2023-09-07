package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;
    @Column
    @NotNull
    private String pw;
    @Column
    @NotNull
    private String name;
    @Column
    @NotNull
    private String phone;

    @Column
    @NotNull
    private String eMail;

    @Column
    @NotNull
    private String birth;

    @Column
    @NotNull
    private String address;

    @Column
    @NotNull
    private String detailaddress;

    @Column
    @NotNull
    private String zipcode;

    @Column(unique = true, name = "nickname")
    @NotNull
    private String nickName;

    @Column(name = "isshow")
    @NotNull
    private String isShow;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Petsitter> petsitterList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Question> questionList;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessageList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Matching> matchingList;


    @Builder
    public Member(Long id, String memberId, String pw, String name, String phone, String eMail, String birth, String address, String detailaddress, String zipcode, String nickName, String isShow, List<Petsitter> petsitterList) {
        this.id = id;
        this.memberId = memberId;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.eMail = eMail;
        this.birth = birth;
        this.address = address;
        this.detailaddress = detailaddress;
        this.zipcode = zipcode;
        this.nickName = nickName;
        this.isShow = isShow;
        this.petsitterList = petsitterList;
    }

}
