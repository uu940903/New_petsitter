package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean login(MemberDTO memberDTO) {
        String id = memberDTO.getId();
        String pw = memberDTO.getPw();

        // ID와 PW로 회원 조회
        Member member = memberRepository.findById(id).orElse(null);
        // 회원이 존재하고 비밀번호가 일치하면 로그인 성공
        if (member != null && pw.equals(member.getPw())) {
            return true;
        }
        // 회원이 존재하지 않거나 비밀번호가 일치하지 않으면 로그인 실패
        return false;
    }
    public MemberDTO getMember(String id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return new MemberDTO(member.getId(), member.getName(), member.getNickname());
        } else {
            throw new IllegalArgumentException("Invalid memberId");
        }
    }

}