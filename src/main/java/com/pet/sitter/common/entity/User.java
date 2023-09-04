package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키
    @Column
    private String userid;
    @Column
    private String nickname; //유저 이름
    @Column
    private String password; //유저 비밀번호
    @Column
    private String email; //유저 구글 이메일
    @Column
    private String role; //유저 권한 (일반 유저, 관리자)

    @Builder
    public User(String userid, String nickname, String password, String email, String role) {
        this.userid=userid;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User() {
    }
}
