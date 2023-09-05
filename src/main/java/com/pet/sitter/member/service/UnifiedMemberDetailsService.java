package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.User;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.member.repository.UserRepository;
import com.pet.sitter.member.validation.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class UnifiedMemberDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // User 테이블에서 username을 이용하여 사용자 정보를 가져옴
        Optional<User> userOptional = userRepository.findByuserid(username);

        // Member 테이블에서 username을 이용하여 사용자 정보를 가져옴
        Optional<Member> memberOptional = memberRepository.findBymemberId(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // UserDetails로 변환하여 반환
            return new org.springframework.security.core.userdetails.User(
                    user.getUserid(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            List<GrantedAuthority> authorities = new ArrayList<>();

            // username이 "admin"인 경우 "ROLE_ADMIN" 권한을 추가
            if ("admin".equals(username)) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            } else {
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            }

            // Member 정보를 기반으로 UserDetails 객체를 생성하여 반환
            return new CustomUserInfo(
                    member.getMemberId(),
                    member.getPw(),
                    authorities,
                    member.getName(),  // id 값을 설정하세요.
                    member.getMemberId(),  // memberId 값을 설정하세요.
                    member.getNickname()
            );
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
