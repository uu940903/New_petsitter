package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Member {

    @Id
    private String id;


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
    private String e_mail;

    @Column
    @NotNull
    private String birth;

    @Column
    @NotNull
    private String address;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private String isshow;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Petsitter> petsitterList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Question> questionList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ChatMessages> chatMessagesList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Matching> matchingList;

}
