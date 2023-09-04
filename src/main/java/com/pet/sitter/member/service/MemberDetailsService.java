/*
package com.pet.sitter.member.service;

import com.pet.sitter.common.entity.User;
import com.pet.sitter.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> memberOptional = userRepository.findByuserid(username);
        if (memberOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        User user = memberOptional.get();

        // UserDetails로 변환하여 반환
        return new org.springframework.security.core.userdetails.User(
                user.getUserid(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
*/
