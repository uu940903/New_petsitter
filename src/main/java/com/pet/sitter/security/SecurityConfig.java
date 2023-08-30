package com.pet.sitter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration//스프링의 환경설정 파일임을 공지
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티 제어를 받도록 만든다.
//로그인 요청 주소는 "/user/login"이며, 로그인 성공 시 "/"로 이동해라
@EnableMethodSecurity(prePostEnabled = true)//@PreAuthorize("isAuthenticated()")//로그인 증가 동작할 수 있기 위함
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .headers((headers) -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin)->formLogin.loginPage("/member/login").defaultSuccessUrl("/main"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }
    @Bean //PasswordEncoder 생성
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //참고 교재 615
    /*Authentication - 인증 -> 자신을 증명하는 것
    AuthenticationManager(인증매니저) - 인증담당
    * */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
