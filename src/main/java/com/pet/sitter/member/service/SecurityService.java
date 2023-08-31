package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.member.validation.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class SecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    //사용자 이름으로 (비밀번호)조회
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        //제시한 username을 가진 사용자가 존재하는지 체크
        Optional<Member> member = memberRepository.findBymemberId(memberId);
        if(member.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        //제시한 username을 가진 사용자가 존재하는 경우 아래 코드 진행
        Member member1=member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        //username이 "admin"이라면
        if ("admin".equals(memberId)) { //"ROLE_ADMIN"을 권한목록추가 -> 권한부여
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else { //그 외의 경우 "ROLE_USER"을 권한목록추가 -> USER권한부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //사용자User 객체리턴
        return new User(member1.getMemberId(), member1.getPw(), authorities);
        //스프링시큐리티는 loadUserByUsername()에 의해 리턴되는 User객체의 비밀번호가
        //화면으로부터 입력한 비번과 일치하는지 검사하는 로직이 내부적으로 존재
    }
}
