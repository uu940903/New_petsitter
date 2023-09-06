package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.member.validation.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member create(UserCreateForm userCreateForm) {
        //SiteUser Entity로 UserRepository 연동하지만

        //여기에서는 컨트롤러부터는 DTO로 받아서 작업할 예정
        Member member = new Member();
        member.setMemberId(userCreateForm.getMemberId());
        member.setPw(passwordEncoder.encode(userCreateForm.getPw1()));
        member.setAddress(userCreateForm.getAddress());
        member.setDetailaddress(userCreateForm.getDetailaddress());
        member.setZipcode(userCreateForm.getZipcode());
        member.setNickname(userCreateForm.getNickname());
        member.setName(userCreateForm.getName());
        member.setPhone(userCreateForm.getPhone());
        member.setBirth(userCreateForm.getBirth());
        member.setEMail(userCreateForm.getEMail());
        member.setIsshow("Y");
        //암호는 스프링시큐리티를 이용해서 암호화하여 비번을 저장할 예정
        memberRepository.save(member);
        return member;
    }
    //user정보조회
    public Member getUser(String memberId) {
        Optional<Member> member2 = memberRepository.findBymemberId(memberId);
        if(member2.isPresent()) {
            return member2.get();
        }else {
            throw new DataNotFoundException("siteUser NOT FOUND");
        }
    }

}