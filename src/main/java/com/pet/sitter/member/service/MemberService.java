package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.member.validation.UserCreateForm;
import jakarta.transaction.Transactional;
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

    //email 중복 검사
    @Transactional
    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    //user정보조회
//    public MemberDTO getUser(MemberDTO memberDTO) {
//        Optional<Member> member2 = memberRepository.findBymemberId(memberDTO.getMemberId());
//        if(member2.isPresent()) {
//            Member member = member2.get();
//            return MemberDTO.toMemberDTO(member);
//        }else {
//            throw new DataNotFoundException("member NOT FOUND");
//        }
//    }

    public Member getUser(String username) {
        Optional<Member> optionalSiteUser = memberRepository.findBymemberId(username);
        if (optionalSiteUser.isPresent()) {
            Member user = optionalSiteUser.get();

            return user;
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    public Member findByMemberId(String memberId) {
        Optional<Member> optionalMember = memberRepository.findBymemberId(memberId);
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        return memberRepository.findBymemberId(memberId).orElse(null);
    }

    public void saveOrUpdate(Member member) {
        memberRepository.save(member);
    }

    public boolean updatePassword(String memberId, String newPassword1) {
        Optional<Member> userOptional = memberRepository.findBymemberId(memberId);

        if (userOptional.isPresent()) {
            Member member = userOptional.get();

            // 여기서 새 비밀번호를 해시화하거나 다른 보안 절차를 수행해야 합니다.
            // 이 예제에서는 간단히 새 비밀번호를 저장합니다.
            member.setPw(newPassword1);

            // 변경된 비밀번호를 데이터베이스에 저장
            memberRepository.save(member);

            return true; // 비밀번호 업데이트 성공
        }

        return false; // 사용자를 찾을 수 없는 경우 또는 업데이트 실패
    }
}
