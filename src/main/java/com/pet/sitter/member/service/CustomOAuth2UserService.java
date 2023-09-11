package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.User;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.member.repository.UserRepository;
import com.pet.sitter.member.validation.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // User 테이블에서 username을 이용하여 사용자 정보를 가져옴
            // Member 테이블에서 username을 이용하여 사용자 정보를 가져옴
        Optional<Member> memberOptional = memberRepository.findBymemberId(username);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // memberid "admin"인 경우 "ROLE_ADMIN" 권한을 추가
            if ("admin".equals(member.getMemberId())) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
                System.out.println("UserRole.ADMIN.getValue(): " + UserRole.ADMIN.getValue());
            } else {
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            }

            System.out.println("Member found: " + member.getMemberId()); // 로그 문구 추가

            return new org.springframework.security.core.userdetails.User(
                    member.getMemberId(),
                    member.getPw(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String id = oAuth2User.getAttributes().get("id").toString();
        System.out.println(oAuth2User.getAttributes());

        Optional<User> existingMemberOpt = userRepository.findByMemberId(id);

        if (existingMemberOpt.isPresent()) {
            UserDetails userDetails = loadUserByUsername(existingMemberOpt.get().getMemberId());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return new DefaultOAuth2User(null, oAuth2User.getAttributes(), userNameAttributeName);
        } else {
            User user1 = new User();

            Map<String, Object> kakaoAccountMap =(Map<String, Object>) oAuth2User.getAttribute("kakao_account");
            String email =(String)kakaoAccountMap.get("email");

            Map<String, Object> propertiesMap=(Map<String,Object>)oAuth2User.getAttribute("properties");
            String nickname=(String)propertiesMap.get("nickname");

            user1.setMemberId(email);
            user1.setPassword(passwordEncoder.encode("defaultPassword"));
            user1.setName(nickname);

            userRepository.save(user1);


            return new DefaultOAuth2User(null,oAuth2User.getAttributes(),userNameAttributeName);
        }
    }
}
