package com.pet.sitter.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String memberId;
    @Column
    private String nickname; //유저 이름
    @Column
    private String password; //유저 비밀번호
    @Column
    private String name;
    @Column
    private String role; //유저 권한 (일반 유저, 관리자)

    @Builder
    public User(String memberId, String nickname, String password, String name, String role) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public User() {
    }
}
