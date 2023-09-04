package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.User;
import com.pet.sitter.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final UnifiedMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 추가

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // DB 저장로직이 필요하면 추가

        String id = oAuth2User.getAttributes().get("id").toString();
        System.out.println(oAuth2User.getAttributes());


        // 기존에 동일한 ID로 가입된 사용자가 있는지 확인하고 처리하는 로직 예시입니다.
        Optional<User> member = userRepository.findByuserid(id);

        if (member.isPresent()) {
            // 이미 가입된 사용자인 경우에 대한 처리 (예: 로그인 처리)
            UserDetails userDetails = memberDetailsService.loadUserByUsername(member.get().getUserid());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);

        } else {
            // 새로운 사용자인 경우에 대한 처리 (예: 회원 가입)
            User user1 = new User();

            // Set the user's id and nickname from Kakao API response


            Map<String, Object> kakaoAccountMap = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
            String email = (String) kakaoAccountMap.get("email");

            Map<String, Object> propertiesMap = (Map<String,Object>) oAuth2User.getAttribute("properties");
            String nickname = (String) propertiesMap.get("nickname");

            user1.setUserid(email);
            user1.setPassword(passwordEncoder.encode("defaultPassword"));  // 비밀번호 설정 및 암호화
            user1.setNickname(nickname);

            userRepository.save(user1);  // 사용자 정보 저장

            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
        }
    }
}
