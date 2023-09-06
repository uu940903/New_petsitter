package com.pet.sitter.member.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
@Getter
public class CustomUserInfo extends org.springframework.security.core.userdetails.User {

    private String name;
    private String memberId;

    private String nickname;

    public CustomUserInfo(String username, String password,
                          Collection<? extends GrantedAuthority> authorities,
                          String name, String memberId, String nickname) {

        super(username, password, authorities);
        this.name = name;
        this.memberId = memberId;
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }
    public String getMemberId() {
        return memberId;
    }
}