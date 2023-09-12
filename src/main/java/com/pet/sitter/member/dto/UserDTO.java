package com.pet.sitter.member.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키

    private String memberId;
    private String name; //유저 이름
    private String password; //유저 비밀번호
    private String nickname; //유저 구글 이메일
    private String role; //유저 권한 (일반 유저, 관리자)

    @Builder
    public UserDTO(String memberId, String name, String password, String nickname, String role) {
        this.memberId = memberId;
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public UserDTO() {
    }
}
